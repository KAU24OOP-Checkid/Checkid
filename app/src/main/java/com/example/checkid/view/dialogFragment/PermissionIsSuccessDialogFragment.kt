package com.example.checkid.view.dialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.checkid.databinding.DialogFragmentPermissionIsSuccessBinding
import com.example.checkid.view.activity.MainActivity


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

            (activity as? MainActivity)?.check()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}