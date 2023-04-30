package com.example.provamobile

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.provamobile.data.AppDataBase
import com.example.provamobile.data.SingletonDatabase
import com.example.provamobile.data.dao.taskDAO
import com.example.provamobile.data.models.task
import com.example.provamobile.databinding.FragmentFormBinding

class FormFragment : Fragment() {

   private lateinit var binding : FragmentFormBinding
    private lateinit var db : AppDataBase
    private lateinit var taskDao : taskDAO
    private var taskId : Int? = null
    private var userId : Int? = null
   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormBinding.inflate(inflater, container, false)
       this.db = SingletonDatabase.getInstance(requireContext().applicationContext)
       taskDao = db.taskDao()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            taskId = it.getInt("TASK_ID", -1)
            userId = it.getInt("USER_ID", -1)
        }

        taskId?.let {
            var task = taskDao.getById(it)

            binding.tituloInput.setText(task?.title)
            binding.descricaoInput.setText(task?.description)

            binding.excluirBtn.setOnClickListener{ mostrarAvisoDeExclusao(taskId) }
        }

        taskId ?: binding.excluirBtn.setOnClickListener {
            var toast = Toast(requireContext().applicationContext)
            toast.setText(R.string.mensagem_tarefa_nao_existe)
            toast.show()
        }

        binding.salvarBtn.setOnClickListener { salvarTarefa(taskId) }
    }

    private fun salvarTarefa(id: Int?){
        val navigation = findNavController()
        val titulo = binding.tituloInput.text.toString()
        val descricao = binding.descricaoInput.text.toString()

        if(tarefaValida()){

            var task = task(null, titulo, descricao, userId!!)

            if(id == -1){
                taskDao.insert(task)
            }
            else{
                task.uid = id
                taskDao.update(task)
            }

            navigation.navigateUp()
        }
        else{
            var toast = Toast(requireContext().applicationContext)
            toast.setText("Dados inválidos. Verifique se o título e descrição foram preenchidos corretamente.")
            toast.show()
        }
    }

    private fun tarefaValida() : Boolean{
        val titulo = binding.tituloInput.text.toString()
        val descricao = binding.descricaoInput.text.toString()

        if(titulo == "" || titulo == null || descricao == "" || descricao == null) return false

        return true
    }

    private fun excluirTarefa(id: Int){
        val navigation = findNavController()
        val tarefa = taskDao.getById(id)

        tarefa?.let {
            taskDao.delete(tarefa)
        }
        navigation.navigate(R.id.listFragment)
    }

    private fun mostrarAvisoDeExclusao(id: Int?){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        alertDialogBuilder.setTitle(R.string.confirmar_exclusao)
        alertDialogBuilder.setMessage(R.string.exluir_item)

        alertDialogBuilder.setPositiveButton(R.string.sim) { _, _ ->
            if (id != null) {
                excluirTarefa(id)
            }
        }

        alertDialogBuilder.setNegativeButton(R.string.nao) { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.show()
    }
}