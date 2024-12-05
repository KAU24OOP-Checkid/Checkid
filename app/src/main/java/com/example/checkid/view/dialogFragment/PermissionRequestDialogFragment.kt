package com.example.checkid.view.dialogFragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class PermissionRequestDialogFragment: DialogFragment() {

    interface PermissionRequestListener {
        fun onPermissionGranted()
    }

    private var listener: PermissionRequestListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("Hi!")
            .setPositiveButton("Accept") {_, _ -> null}
            .create()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}