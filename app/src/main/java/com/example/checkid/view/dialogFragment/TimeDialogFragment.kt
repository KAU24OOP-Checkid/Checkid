package com.example.checkid.view.dialogFragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.checkid.databinding.DialogTimeBinding
import java.util.*

class TimeDialogFragment : DialogFragment() {

    private var _binding: DialogTimeBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val REQUEST_KEY = "timeRequestKey"
        const val BUNDLE_KEY_HOUR = "hour"
        const val BUNDLE_KEY_MINUTE = "minute"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogTimeBinding.inflate(layoutInflater)

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        binding.timePicker.setIs24HourView(true)
        binding.timePicker.hour = hour
        binding.timePicker.minute = minute

        return AlertDialog.Builder(requireContext())
            .setTitle("시간 설정")
            .setView(binding.root)
            .setPositiveButton("확인") { dialog, which ->
                val selectedHour = binding.timePicker.hour
                val selectedMinute = binding.timePicker.minute

                // Fragment Result API를 사용하여 결과 전달
                val result = Bundle().apply {
                    putInt(BUNDLE_KEY_HOUR, selectedHour)
                    putInt(BUNDLE_KEY_MINUTE, selectedMinute)
                }
                setFragmentResult(REQUEST_KEY, result)
            }
            .setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}