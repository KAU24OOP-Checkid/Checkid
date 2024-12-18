package com.example.checkid.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.checkid.R
import com.example.checkid.databinding.FragmentPermissionBinding
import com.example.checkid.model.PermissionManager
import com.example.checkid.view.dialogFragment.PermissionIsFailDialogFragment
import com.example.checkid.view.dialogFragment.PermissionIsSuccessDialogFragment
import com.example.checkid.viewmodel.PermissionViewModel
import kotlinx.coroutines.launch

class PermissionFragment: Fragment(R.layout.fragment_permission) {
    private var _binding : FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PermissionViewModel by viewModels()

    private val requestPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.forEach { (permission, isGranted) ->
            if (isGranted) {

            }

            else {

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)

        val permissions = viewModel.getAllPermissions()

        viewModel.permissionResult.observe(viewLifecycleOwner) { hasPermission ->
            when (hasPermission) {
                true -> PermissionIsSuccessDialogFragment().show(childFragmentManager, "SuccessDialog")
                false -> PermissionIsFailDialogFragment().show(childFragmentManager, "FailDialog")
                null -> {}
            }
        }

        requestPermissions()

        binding.permissionsTextView.text = permissions.joinToString("\n") { permission ->
            "- $permission"
        }

        binding.checkPermissionButton.setOnClickListener {
            viewModel.checkAllPermissions(requireContext())
        }

        binding.openSettingsButton.setOnClickListener {
            viewModel.openAppSettings(requireContext())
        }

        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissions() {
        val missingPermissions = PermissionManager.getAllPermissions().filter { permission ->
            requireContext().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            requestPermissionsLauncher.launch(missingPermissions.toTypedArray())
        }
    }
}