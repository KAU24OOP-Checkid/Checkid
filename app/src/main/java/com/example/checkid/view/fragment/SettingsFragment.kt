package com.example.checkid.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.checkid.R
import com.example.checkid.databinding.FragmentSettingsBinding
import com.example.checkid.model.TimeSettingRepository
import com.example.checkid.view.dialogFragment.TimeDialogFragment
import com.example.checkid.viewmodel.SettingsViewModel
import com.example.checkid.viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels() {
        SettingsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSettingsBinding.bind(view)

        // LiveData 관찰 및 UI 업데이트
        viewModel.userId.observe(viewLifecycleOwner) { id ->
            binding.tvAccountInfo.text = "My ID: $id"
        }

        viewModel.partnerId.observe(viewLifecycleOwner) { partnerId ->
            binding.tvPartnerId.text = "Partner ID: $partnerId"
        }

        // Account Info 버튼 클릭 리스너
        binding.btnAccountInfo.setOnClickListener {
            lifecycleScope.launch {
                viewModel.fetchUserId(requireContext())
            }
        }

        // Account Connect 버튼 클릭 리스너
        binding.btnAccountConnect.setOnClickListener {
            lifecycleScope.launch {
                viewModel.fetchPartnerUserId(requireContext())
            }
        }

        // Map Fragment로 이동 버튼 클릭 리스너
        binding.btnOpenMap.setOnClickListener {
           // findNavController().navigate(R.id.action_settingsFragment_to_mapsFragment)
        }

        // 시간 설정 버튼 클릭 리스너 설정
        binding.btnSetTime.setOnClickListener {
            // TimeDialogFragment 열기
            val timeDialog = TimeDialogFragment()
            timeDialog.show(childFragmentManager, "TimeDialogFragment")
        }

        // Fragment Result API를 사용하여 TimeDialogFragment로부터 결과 받기
        childFragmentManager.setFragmentResultListener(
            TimeDialogFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            if (requestKey == TimeDialogFragment.REQUEST_KEY) {
                val selectedHour = bundle.getInt(TimeDialogFragment.BUNDLE_KEY_HOUR)
                val selectedMinute = bundle.getInt(TimeDialogFragment.BUNDLE_KEY_MINUTE)
                Log.d("SettingsFragment", "Result received: Hour=$selectedHour, Minute=$selectedMinute")
                onTimeSelected(selectedHour, selectedMinute)
            }else {
                Log.d("SettingsFragment", "Unexpected requestKey: $requestKey")
            }
        }
    }

    /**
     * TimeDialogFragment로부터 시간 선택을 받는 메서드
     */
    private fun onTimeSelected(hour: Int, minute: Int) {
        // 선택된 시간을 문자열로 포맷
        val selectedTime = String.format("%02d:%02d", hour, minute)

        // UI 업데이트
        binding.tvSelectedTime.text = "선택된 시간: $selectedTime"
        Toast.makeText(requireContext(), "시간 설정됨: $selectedTime", Toast.LENGTH_SHORT).show()

        // Firestore에 시간 저장
        lifecycleScope.launch {
            val isSaved = TimeSettingRepository.saveTimeSetting(selectedTime)
            if (isSaved) {
                Log.d("SettingsFragment", "시간 데이터 Firestore에 저장 완료")
            } else {
                Log.e("SettingsFragment", "시간 데이터 Firestore에 저장 실패")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
