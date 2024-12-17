package com.example.checkid.view.dialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.checkid.databinding.DialogFragmentPermissionIsSuccessBinding


class PermissionIsSuccessDialogFragment: DialogFragment() {
    private var _binding: DialogFragmentPermissionIsSuccessBinding? = null
    private val binding get() = _binding!!
    private var listener : PermissionGrantedListener? = null

    interface PermissionGrantedListener {
        fun onPermissionGranted()
    }

    fun setPermissionListener(listener: PermissionGrantedListener) {
        this.listener = listener
    }

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

        val fragment = parentFragment

        binding.permissionIsSuccessButton.setOnClickListener {
            dismiss()

            if (fragment != null) {
                parentFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}