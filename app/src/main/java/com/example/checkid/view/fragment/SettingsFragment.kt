package com.example.checkid.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.checkid.R
import com.example.checkid.databinding.FragmentSettingsBinding
import com.example.checkid.model.DataStoreManager
import com.example.checkid.model.TimeSettingRepository
import com.example.checkid.view.dialogFragment.TimeDialogFragment
import com.example.checkid.viewmodel.SettingsViewModel
import com.example.checkid.viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {
            val id = DataStoreManager.getUserId(requireContext())
            val time = TimeSettingRepository.getTimeSetting(id)

            viewModel.updateTime(time)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LiveData 관찰 및 UI 업데이트
        viewModel.time.observe(viewLifecycleOwner) { time ->
            binding.tvSelectedTime.text = "선택된 시간: $time"
        }

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
            viewModel.fetchChildLocation("child") // Firestore에서 데이터 가져오기

            viewModel.location.observe(viewLifecycleOwner) { location ->
                if (location != null) {
                    val mapsFragment = MapsFragment()
                    val bundle = Bundle().apply {
                        putDouble("latitude", location.latitude)
                        putDouble("longitude", location.longitude)
                    }
                    mapsFragment.arguments = bundle

                    // FragmentTransaction으로 MapsFragment로 이동
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_fragmentContainerView, mapsFragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    Toast.makeText(requireContext(), "위치 데이터를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
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
                Log.d(
                    "SettingsFragment",
                    "Result received: Hour=$selectedHour, Minute=$selectedMinute"
                )
                onTimeSelected(selectedHour, selectedMinute)
            } else {
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
            val isSaved = viewModel.setSelectedTime(requireContext(), selectedTime)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
