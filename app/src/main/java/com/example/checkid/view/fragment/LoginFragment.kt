package com.example.checkid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.checkid.R
import com.example.checkid.databinding.FragmentLoginBinding
import com.example.checkid.view.MainActivity
import com.example.checkid.viewmodel.LoginViewModel
import com.example.checkid.viewmodel.LoginViewModelFactory
import kotlinx.coroutines.launch

class LoginFragment() : Fragment(R.layout.fragment_login) {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels() {
        LoginViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.LoginButton.setOnClickListener {
            val id = binding.LoginID.text.toString()
            val pw = binding.LoginPW.text.toString()
        }

        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}