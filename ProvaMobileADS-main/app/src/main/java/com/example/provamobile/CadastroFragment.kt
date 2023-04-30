package com.example.provamobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.provamobile.data.AppDataBase
import com.example.provamobile.data.SingletonDatabase
import com.example.provamobile.data.dao.userDAO
import com.example.provamobile.data.models.user
import com.example.provamobile.databinding.FragmentCadastroBinding
import com.example.provamobile.databinding.FragmentFormBinding

class CadastroFragment : Fragment() {

    private lateinit var binding : FragmentCadastroBinding
    private lateinit var db : AppDataBase
    private lateinit var userDao : userDAO
    private lateinit var navigation : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCadastroBinding.inflate(inflater, container, false)
        this.db = SingletonDatabase.getInstance(requireContext().applicationContext)
        userDao = db.userDao()
        navigation = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.salvarBtn.setOnClickListener { cadastrarUsuario() }
    }

    private fun cadastrarUsuario(){
        val nome = binding.nomeInput.text.toString()
        val login = binding.loginInput.text.toString()
        val senha = binding.senhaInput.text.toString()

        if(dadosValidos()){
            val user = user(null, nome, login, senha)
            val id = userDao.insert(user)

            val users = userDao.getAll()
            val bundle = Bundle()
            bundle.putInt("USER_ID", id.toInt())
            navigation.navigate(R.id.listFragment, bundle)
        }
        else{
            var toast = Toast(requireContext().applicationContext)
            toast.setText(R.string.dados_invalidos)
            toast.show()
        }
    }

    private fun dadosValidos() : Boolean{
        val nome = binding.nomeInput.text.toString()
        val login = binding.loginInput.text.toString()
        val senha = binding.senhaInput.text.toString()

        return nome != CAMPO_VAZIO && login != CAMPO_VAZIO && senha != CAMPO_VAZIO
    }

    companion object {
        const val CAMPO_VAZIO = ""
    }
}