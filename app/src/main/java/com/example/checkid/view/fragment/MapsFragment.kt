package com.example.checkid.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.checkid.R
import com.example.checkid.model.DataStoreManager
import com.example.checkid.model.LocationRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var locationListener: ListenerRegistration? = null // Firestore 실시간 업데이트 리스너
    private lateinit var childId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            childId = DataStoreManager.getUserPartnerId(requireContext())
            if (childId.isEmpty()) {
                Toast.makeText(requireContext(), "올바르지 않은 자녀 ID입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Firestore에서 초기 위치 데이터 가져오기
        fetchChildLocationAndUpdateMap()

        // Firestore 실시간 업데이트 리스너 설정
        if (::childId.isInitialized && childId.isNotEmpty()) {
            setupRealTimeLocationUpdates()
        } else {
            Toast.makeText(requireContext(), "자녀 ID가 초기화되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchChildLocationAndUpdateMap() {
        lifecycleScope.launch(Dispatchers.Main) {
            val loadingDialog = showLoadingDialog()

            val location = withContext(Dispatchers.IO) {
                LocationRepository.getLocationById(childId)
            }

            loadingDialog.dismiss()

            if (location != null && location.latitude != 0.0 && location.longitude != 0.0) {
                displayLocationOnMap(location.latitude, location.longitude, "자녀 위치")
            } else {
                Toast.makeText(requireContext(), "위치 데이터를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRealTimeLocationUpdates() {
        if (childId.isEmpty()) {
            Toast.makeText(requireContext(), "자녀 ID를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val db = Firebase.firestore
        locationListener = db.collection("Location").document(childId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(requireContext(), "실시간 위치 업데이트 오류", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val latitude = snapshot.getDouble("latitude") ?: 0.0
                    val longitude = snapshot.getDouble("longitude") ?: 0.0
                    displayLocationOnMap(latitude, longitude, "실시간 자녀 위치")
                }
            }
    }

    private fun displayLocationOnMap(latitude: Double, longitude: Double, title: String) {
        val location = LatLng(latitude, longitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(location).title(title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    private fun showLoadingDialog(): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("로딩 중")
            .setMessage("자녀의 위치를 가져오는 중입니다...")
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationListener?.remove() // Firestore 리스너 제거
    }
}
