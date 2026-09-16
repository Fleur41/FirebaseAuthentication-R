package com.sam.firebaseauthentication_r.authentication.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sam.firebaseauthentication_r.authentication.CompanyInfo
import com.sam.firebaseauthentication_r.authentication.EmailAndPasswordContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Sign Up") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),

        ) {
            CompanyInfo(modifier = Modifier.weight(1f))

            EmailAndPasswordContent(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                email = email,
                password = password,
                onEmailChange = {email = it},
                onPasswordChange = {password = it},
                onEmailClear = {email = ""},
                onPasswordClear = {password = ""},
                actionButtonText = "Sign Up",
                showConfirmPasswordField = true,
                confirmPasswordValue = confirmPassword,
                onConfirmPasswordChange = {confirmPassword = it},
                onConfirmPasswordClear = {confirmPassword = ""},
                onActionButtonClick = {
                    authViewModel.signUp(email, password)
                }
            )

            Box(modifier = Modifier.weight(1f)) { }
        }
    }
}