package com.sam.firebaseauthentication_r.authentication

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.reporting.MessagingClientEvent
import com.sam.firebaseauthentication_r.datastore.DatastoreRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
    fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: () -> Unit,
        onSignUpFailure: (Exception) -> Unit
    )
    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    // Send password reset email
    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    // Google Sign-In with ID token
    fun signInWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
    fun signOut()
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): AuthRepository {

    override fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: () -> Unit,
        onSignUpFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                authResult.user?.let {
                    GlobalScope.launch {
                        try {
                            saveToFirestore(it)
                        } catch (e: Exception) {
                            Log.e("TAG", "Firestore save error: ", e)
                        }
                        Log.d("TAG", "authResult: $authResult")
                        onSignUpSuccess()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "exception: $exception")
                onSignUpFailure(exception)
            }
    }

    override fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                authResult.user?.let {
                    Log.d("TAG", "authResult: $authResult")
                    onSuccess()
                }

            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "exception: $exception")
                onFailure(exception)
            }
//        WEB Client ID: 1:183617969557:android:adbbc900da3778becbea9c
//        email: Paul@gmail.com, Kushal@gmail.com,  shigen3030
//        pwd: paul1234, Kushall.com234, shigen1010
    }

    override fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d("TAG", "Password reset email sent successfully to $email")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Failed to send reset email: $exception")
                onFailure(exception)
            }
    }

    override fun signInWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                authResult.user?.let { user ->
                    GlobalScope.launch {
                        try {
                            saveToFirestore(user)
                        } catch (e: Exception) {
                            Log.e("TAG", "Firestore save error: ", e)
                        }
                        Log.d("TAG", "Google Sign-In successful for user: ${user.uid}")
                        onSuccess()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Google Sign-In failed: $exception")
                onFailure(exception)
            }
    }


    suspend fun saveToFirestore(user: FirebaseUser){
        try {
            val usersCollection = firestore.collection("users")
            val usersReference = usersCollection.document(user.uid)
                .get()
                .await()

            if (usersReference.exists()){
                Log.d("TAG", "User ${user.uid} already exists.")
                return
            }

            val userMap = hashMapOf(
                "uid" to user.uid,
                "email" to (user.email ?: ""),
                "displayName" to (user.displayName ?: "")
            )

            usersCollection.document(user.uid)
                .set(userMap)
                .await()
        } catch (e: Exception) {
            Log.e("TAG", "saveToFirestore exception: ", e)
        }
    }
    override fun signOut() {
        auth.signOut()
    }
}