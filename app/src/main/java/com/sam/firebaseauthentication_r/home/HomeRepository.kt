package com.sam.firebaseauthentication_r.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import javax.inject.Inject

interface HomeRepository {
    fun logDetailScreenView()
    fun uploadImage(uri: Uri)
}

class HomeRepositoryImpl @Inject constructor(
    private val analytics: FirebaseAnalytics,
    private val storage: FirebaseStorage
): HomeRepository{
    override fun logDetailScreenView() {
        val metadata = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, "Detail")
        }
        analytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            metadata

        )
    }

    override fun uploadImage(uri: Uri) {
        val filename = uri.lastPathSegment ?: UUID.randomUUID().toString()
        val imageDirectoryReference = storage.reference.child("images")
        val fileReference = imageDirectoryReference.child(filename)
        
        fileReference.putFile(uri)
            .addOnSuccessListener {
                Log.d("TAG", "File $filename uploaded successfully")
            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to upload file $filename")
            }

    }

}