package com.example.checkid.view.dialogFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.checkid.databinding.DialogFragmentLoginIsSuccessBinding


class LoginIsSuccessDialogFragment : DialogFragment() {
    private var _binding: DialogFragmentLoginIsSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        _binding = DialogFragmentLoginIsSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginIsSuccessButton.setOnClickListener {
            dismiss()

            val intent = Intent().apply {
                action = "LOGIN_SUCCESS"
                putExtra("RESULT", "LOGIN_SUCCESS")
            }
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}