package com.example.provamobile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.provamobile.data.AppDataBase
import com.example.provamobile.data.SingletonDatabase
import com.example.provamobile.data.dao.taskDAO
import com.example.provamobile.data.models.task
import com.example.provamobile.databinding.FragmentListBinding


class ListFragment : Fragment() {

    private lateinit var binding : FragmentListBinding
    private lateinit var navigation : NavController
    private lateinit var db : AppDataBase
    private lateinit var taskDao : taskDAO
    private lateinit var listView : ListView
    private var userId : Int? = null
    private val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentListBinding.inflate(inflater, container, false)
       this.db = SingletonDatabase.getInstance(requireContext().applicationContext)
        taskDao = db.taskDao()
        navigation = findNavController()
        listView = binding.listView

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            userId = it.getInt("USER_ID", -1)
        }

        binding.adicionarTarefa.setOnClickListener {
            bundle.putInt("USER_ID", userId!!)
            navigation.navigate(R.id.formFragment, bundle)
        }
        binding.btnDeletarTudo.setOnClickListener{ mostrarAvisoDeExclusao() }

        listarTarefas()
    }

    private fun listarTarefas(){
        var list = taskDao.getByUser(userId!!)
        var taskList = mutableListOf<task>()
        var taskIdList = mutableListOf<Int>()

        list.forEach { (_, tasks) ->
            tasks.forEach { task ->
                if (task.uid != null) {
                    taskList.add(task)
                    taskIdList.add(task.uid!!)
                }
            }
        }

        taskList = taskList.distinct().toMutableList()
        taskIdList = taskIdList.distinct().toMutableList()

        var context = activity?.baseContext as Context
        var resource = android.R.layout.simple_list_item_1

        var adapter : ArrayAdapter<task> = ArrayAdapter(context, resource, taskList)

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val taskId = taskIdList[position]

            bundle.putInt("TASK_ID", taskId)
            navigation.navigate(R.id.formFragment, bundle)
        }
    }

    private fun mostrarAvisoDeExclusao(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        alertDialogBuilder.setTitle(R.string.confirmar_exclusao)
        alertDialogBuilder.setMessage(R.string.mensagem_exclusao)

        alertDialogBuilder.setPositiveButton(R.string.sim) { _, _ ->
            deletarTarefa()
        }

        alertDialogBuilder.setNegativeButton(R.string.nao) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }

    private fun deletarTarefa() {
        taskDao.deleteAllTasks()

        val adapter = listView.adapter as ArrayAdapter<String>
        adapter.clear()
        adapter.notifyDataSetChanged()
    }
}