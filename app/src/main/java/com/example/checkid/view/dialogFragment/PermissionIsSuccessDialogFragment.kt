package com.example.checkid.view.dialogFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.checkid.databinding.DialogFragmentPermissionIsSuccessBinding


class PermissionIsSuccessDialogFragment: DialogFragment() {
    private var _binding: DialogFragmentPermissionIsSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentPermissionIsSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.permissionIsSuccessButton.setOnClickListener {
            dismiss()

            val intent = Intent().apply {
                action = "PERMISSION_SUCCESS"
                putExtra("RESULT", "PERMISSION_SUCCESS")
            }
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}