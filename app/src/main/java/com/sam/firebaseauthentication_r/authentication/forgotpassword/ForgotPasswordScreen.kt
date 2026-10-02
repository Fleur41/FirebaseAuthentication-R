package com.sam.firebaseauthentication_r.authentication.forgotpassword

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.messaging.FcmBroadcastProcessor.reset
import com.sam.firebaseauthentication_r.authentication.EmailAndPasswordContent
import com.sam.firebaseauthentication_r.authentication.signup.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit
) {

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Password",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Please enter the email address affiliated with your account.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF2E7D32), // Dark green text
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            EmailAndPasswordContent(
                modifier = Modifier.fillMaxWidth(),
                showEmailField = true,
                showPasswordField = false,
                email = email,
                onEmailChange = { email = it },
                onEmailClear = { email = "" },
                onPasswordChange = {},
                onPasswordClear = {},
                actionButtonContent = {
                    Text(text = "Send Reset Link")
                },
                onActionButtonClick = {
                    if (email.isBlank()) {
                        Toast.makeText(context, "Please enter your email address", Toast.LENGTH_SHORT).show()
                        return@EmailAndPasswordContent
                    }

                    // Trigger Firebase reset email
                    authViewModel.sendPasswordResetEmail(email.trim())
                    Toast.makeText(context, "Password reset link sent to $email", Toast.LENGTH_LONG).show()
                    onBackClick()
                }
            )
        }
    }
}