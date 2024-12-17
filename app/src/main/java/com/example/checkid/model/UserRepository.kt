package com.example.checkid.model

import ChildUser
import User
import ParentUser
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

object UserRepository {
    private const val COLLECTION_PARENT = "ParentUser"
    private const val COLLECTION_CHILD = "ChildUser"

    private const val DOCUMENT_ID = "id"
    private const val DOCUMENT_PW = "pw"
    private const val DOCUMENT_PARTNER_ID = "partnerId"
    private const val DOCUMENT_REPORT_TIME = "reportTime"

    private const val DEFAULT_ID = ""
    private const val DEFAULT_PASSWORD = ""
    private const val DEFAULT_PARTNER_ID = ""
    private const val DEFAULT_REPORT_TIME = 4800

    suspend fun getUserById(id: String) : User? {
        val db = Firebase.firestore

        try {
            val parentUserQuery = db.collection(COLLECTION_PARENT)
                .whereEqualTo(DOCUMENT_ID, id)
                .get()
                .await()

            if (!parentUserQuery.isEmpty) {
                val document = parentUserQuery.documents.first()
                return ParentUser(
                    id = document.getString(DOCUMENT_ID) ?: DEFAULT_ID,
                    pw = document.getString(DOCUMENT_PW) ?: DEFAULT_PASSWORD
                ).apply {
                    partnerId = document.getString(DOCUMENT_PARTNER_ID) ?: DEFAULT_PARTNER_ID
                    reportTime = document.getLong(DOCUMENT_REPORT_TIME)?.toInt() ?: DEFAULT_REPORT_TIME
                }
            }

            val childUserQuery = db.collection(COLLECTION_CHILD)
                .whereEqualTo(DOCUMENT_ID, id)
                .get()
                .await()

            if (!childUserQuery.isEmpty) {
                val document = childUserQuery.documents.first()

                return ChildUser(
                    id = document.getString(DOCUMENT_ID) ?: DEFAULT_ID,
                    pw = document.getString(DOCUMENT_PW) ?: DEFAULT_PASSWORD
                ).apply {
                    partnerId = document.getString(DOCUMENT_PARTNER_ID) ?: DEFAULT_PARTNER_ID
                    // ApplicationList 받아오는 logic 추가
                }
            }

            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    suspend fun getUserByIdAndPassword(id: String, pw: String): User? {
        val db = Firebase.firestore

        try {
            val parentUserQuery = db.collection(COLLECTION_PARENT)
                .whereEqualTo(DOCUMENT_ID, id)
                .whereEqualTo(DOCUMENT_PW, pw)
                .get()
                .await()

            if (!parentUserQuery.isEmpty) {
                val document = parentUserQuery.documents.first()
                return ParentUser(
                    id = document.getString(DOCUMENT_ID) ?: DEFAULT_ID,
                    pw = document.getString(DOCUMENT_PW) ?: DEFAULT_PASSWORD
                ).apply {
                    partnerId = document.getString(DOCUMENT_PARTNER_ID) ?: DEFAULT_PARTNER_ID
                    reportTime = document.getLong(DOCUMENT_REPORT_TIME)?.toInt() ?: DEFAULT_REPORT_TIME
                }
            }

            val childUserQuery = db.collection(COLLECTION_CHILD)
                .whereEqualTo(DOCUMENT_ID, id)
                .whereEqualTo(DOCUMENT_PW, pw)
                .get()
                .await()

            if (!childUserQuery.isEmpty) {
                val document = childUserQuery.documents.first()
                return ChildUser(
                    id = document.getString(DOCUMENT_ID) ?: DEFAULT_ID,
                    pw = document.getString(DOCUMENT_PW) ?: DEFAULT_PASSWORD
                ).apply {
                    partnerId = document.getString(DOCUMENT_PARTNER_ID) ?: DEFAULT_PARTNER_ID
                    // ApplicationList 받아오는 logic 추가
                }
            }

            return null

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun isParent(user: User): Boolean? {
        return when (user) {
            is ParentUser -> true
            is ChildUser -> false
            else -> null
        }
    }

    fun getUserType(user: User): String? {
        return when (user) {
            is ParentUser -> "Parent"
            is ChildUser -> "Child"
            else -> null
        }
    }
}