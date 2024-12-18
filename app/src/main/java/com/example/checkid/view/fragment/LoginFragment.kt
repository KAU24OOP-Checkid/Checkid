package com.example.checkid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.checkid.R
import com.example.checkid.databinding.FragmentLoginBinding
import com.example.checkid.view.dialogFragment.LoginIsFailDialogFragment
import com.example.checkid.view.dialogFragment.LoginIsSuccessDialogFragment
import com.example.checkid.viewmodel.LoginViewModel
import com.example.checkid.viewmodel.LoginViewModelFactory

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

        viewModel.loginResult.observe(viewLifecycleOwner) { isLogin ->
            when (isLogin) {
                true -> LoginIsSuccessDialogFragment().show(childFragmentManager, "SuccessDialog")
                false -> LoginIsFailDialogFragment().show(childFragmentManager, "FailDialog")
                null -> {}
            }
        }

        binding.loginButton.setOnClickListener {
            val id = binding.loginId.text.toString()
            val password = binding.loginPassword.text.toString()

            viewModel.login(requireContext(), id, password)
        }

        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}