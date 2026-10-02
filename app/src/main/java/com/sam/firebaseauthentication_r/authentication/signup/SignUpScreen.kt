package com.sam.firebaseauthentication_r.authentication.signup

import android.R.id.message
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sam.firebaseauthentication_r.authentication.CompanyInfo
import com.sam.firebaseauthentication_r.authentication.EmailAndPasswordContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,

) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
       if (authState is AuthState.Success) {
           // We don'ty need to explicitly to navigate to HomeScreen as it will be taken core of by statFlow in SettingsViewModel
       }
    }
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
            CompanyInfo(modifier = Modifier.weight(0.8f))

            EmailAndPasswordContent(
                modifier = Modifier
                    .weight(1.8f)
                    .padding(8.dp),
                email = email,
                password = password,
                onEmailChange = {email = it},
                onPasswordChange = {password = it},
                onEmailClear = {email = ""},
                onPasswordClear = {password = ""},
                enableActionButton = authState !is AuthState.Loading,
                actionButtonContent = {
                    if (authState is AuthState.Loading){
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text(text = "Sign Up")
                    }
                },
                showConfirmPasswordField = true,
                confirmPasswordValue = confirmPassword,
                onConfirmPasswordChange = {confirmPassword = it},
                onConfirmPasswordClear = {confirmPassword = ""},
                onActionButtonClick = {
                    if (email.isBlank() || password.isBlank()){
                        Toast.makeText(context, "Please enter email/password", Toast.LENGTH_SHORT).show()
                        return@EmailAndPasswordContent
                    }
                    authViewModel.signUp(email.trim(), password.trim())
                }
            )


            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if(authState is AuthState.Error){
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}