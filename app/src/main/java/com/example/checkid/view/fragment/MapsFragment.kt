package com.example.checkid.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.checkid.R
import com.example.checkid.model.Location
import com.example.checkid.utils.FirebaseHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble("latitude", 0.0)
            longitude = it.getDouble("longitude", 0.0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // SupportMapFragment 초기화
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    /**
     * Google Map이 준비되었을 때 호출됩니다.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (latitude != 0.0 && longitude != 0.0) {
            // 전달받은 위치 데이터를 지도에 표시
            val childLocation = LatLng(latitude, longitude)
            mMap.addMarker(MarkerOptions().position(childLocation).title("자녀 위치"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(childLocation, 15f))
        } else {
            // 위치 데이터가 없을 경우, Firebase에서 실시간으로 가져오기
            fetchChildLocationAndUpdateMap()
        }

        // 실시간 위치 업데이트 (선택 사항)
        FirebaseHelper.listenToChildLocation("child1") { newLocation ->
            if (newLocation != null) {
                val updatedLatLng = LatLng(newLocation.latitude, newLocation.longitude)
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(updatedLatLng).title("자녀 위치"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(updatedLatLng, 15f))
            }
        }
    }

    /**
     * 위치 데이터가 없을 경우 Firebase에서 가져와 지도에 표시하는 함수
     */
    private fun fetchChildLocationAndUpdateMap()   {
        val childId = "child1" // 필요에 따라 동적으로 설정

        CoroutineScope(Dispatchers.Main).launch {
            // 위치 데이터를 가져오는 동안 로딩 다이얼로그 표시
            val loadingDialog = AlertDialog.Builder(requireContext())
                .setTitle("로딩 중")
                .setMessage("자녀의 위치를 가져오는 중입니다...")
                .setCancelable(false)
                .show()

            val location: Location? = FirebaseHelper.getChildLocation(childId)

            loadingDialog.dismiss()

            if (location != null && location.latitude != 0.0 && location.longitude != 0.0) {
                // 위치 데이터를 지도에 표시
                val childLocation = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(childLocation).title("자녀 위치"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(childLocation, 15f))
            } else {
                // 위치 데이터를 가져오지 못했을 때 처리
                Toast.makeText(requireContext(), "자녀의 위치를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
