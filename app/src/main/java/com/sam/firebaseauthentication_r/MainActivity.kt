package com.sam.firebaseauthentication_r

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sam.firebaseauthentication_r.authentication.signin.SignInScreen
import com.sam.firebaseauthentication_r.authentication.signup.SignUpScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            SignInScreen()
//
            FirebaseApp()
        }
    }
}

