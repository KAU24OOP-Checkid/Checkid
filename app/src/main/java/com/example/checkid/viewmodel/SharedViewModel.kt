package com.example.checkid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SharedViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // LiveData for ParentUser ID
    private val _parentUserId = MutableLiveData<String>()
    val parentUserId: LiveData<String> get() = _parentUserId

    // LiveData for ParentUser PartnerId
    private val _partnerId = MutableLiveData<String>()
    val partnerId: LiveData<String> get() = _partnerId

    // Function to fetch ParentUser ID
    suspend fun fetchParentUserId() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                val parentUserDoc = firestore.collection("ParentUser")
                    .document(currentUser.uid)
                    .get()
                    .await()
                if (parentUserDoc.exists()) {
                    val parentId = parentUserDoc.getString("parent")
                    _parentUserId.postValue(parentId ?: "정보 없음")
                } else {
                    _parentUserId.postValue("ParentUser 문서 없음")
                }
            } catch (e: Exception) {
                _parentUserId.postValue("오류 발생: ${e.message}")
            }
        } else {
            _parentUserId.postValue("로그인되지 않음")
        }
    }

    // Function to fetch PartnerId
    suspend fun fetchPartnerId() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                val parentUserDoc = firestore.collection("ParentUser")
                    .document(currentUser.uid)
                    .get()
                    .await()
                if (parentUserDoc.exists()) {
                    val partnerId = parentUserDoc.getString("PartnerId")
                    _partnerId.postValue(partnerId ?: "정보 없음")
                } else {
                    _partnerId.postValue("ParentUser 문서 없음")
                }
            } catch (e: Exception) {
                _partnerId.postValue("오류 발생: ${e.message}")
            }
        } else {
            _partnerId.postValue("로그인되지 않음")
        }
    }
}
