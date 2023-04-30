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
import com.example.provamobile.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var db : AppDataBase
    private lateinit var navigation : NavController
    private lateinit var userDao : userDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        this.db = SingletonDatabase.getInstance(requireContext().applicationContext)
        navigation = findNavController()
        userDao = db.userDao()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cadastrarBtn.setOnClickListener{ navigation.navigate(R.id.cadastroFragment) }
        binding.logarBtn.setOnClickListener{ doLogin() }
    }

    private fun exibirMensagemDeErro() {
        val toast = Toast(requireContext().applicationContext)
        toast.setText(R.string.dados_invalidos)
        toast.show()
    }

    private fun doLogin() {
        val login = binding.loginInput.text.toString()
        val senha = binding.senhaInput.text.toString()

        if (dadosValidos()) {
            val user = userDao.login(login, senha)

            if (user != null) {
                val bundle = Bundle()
                bundle.putInt("USER_ID", user.uid!!)
                navigation.navigate(R.id.listFragment, bundle)
            } else {
                exibirMensagemDeErro()
            }
        } else {
            exibirMensagemDeErro()
        }
    }

    private fun dadosValidos() : Boolean{
        val login = binding.loginInput.text.toString()
        val senha = binding.senhaInput.text.toString()

        return login != CAMPO_VAZIO && senha != CAMPO_VAZIO
    }

    companion object {
        const val CAMPO_VAZIO = ""
    }
}