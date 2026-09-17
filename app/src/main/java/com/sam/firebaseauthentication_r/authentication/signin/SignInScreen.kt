package com.sam.firebaseauthentication_r.authentication.signin

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.sam.firebaseauthentication_r.R
import com.sam.firebaseauthentication_r.authentication.CompanyInfo
import com.sam.firebaseauthentication_r.authentication.EmailAndPasswordContent
import com.sam.firebaseauthentication_r.authentication.signup.AuthState
import com.sam.firebaseauthentication_r.authentication.signup.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onSignUpClick: () -> Unit,
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            // We don'ty need to explicitly to navigate to HomeScreen as it will be taken core of by statFlow in SettingsViewModel
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Sign In") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            CompanyInfo(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                EmailAndPasswordContent(
                    email = email,
                    password = password,
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onEmailClear = { email = "" },
                    onPasswordClear = { password = "" },
                    actionButtonContent = {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = LocalContentColor.current
                            )
                        } else {
                            Text(text = "Sign In")
                        }
                    },
                    onActionButtonClick = {
                        if (email.isBlank() || password.isBlank()){
                            Toast.makeText(context, "Please enter email/password", Toast.LENGTH_SHORT).show()
                            return@EmailAndPasswordContent
                        }
                        authViewModel.signIn(email.trim(), password.trim())
                    }
                )

                Box() {
                    if(authState is AuthState.Error){
                        Text(
                            text = (authState as AuthState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            SignUpBox(
                modifier = Modifier.weight(1f),
                onSignUpClick = onSignUpClick
            )
        }
    }
}


@Composable
fun SignUpBox(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ){
        Row(){
            Text(
                text = "Don't have an account?",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier.clickable{onSignUpClick()},
                text = "Sign up instead?",
                style = MaterialTheme.typography.titleMedium,
                textDecoration = TextDecoration.Underline,
                color = Color.Blue
            )
        }

    }
}



@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    onClear: () -> Unit,
    isPasswordField: Boolean = false
) {
    var showPassword by remember { mutableStateOf(false) }
    var passwordResouce by remember (showPassword){ mutableIntStateOf(if (showPassword) R.drawable.ic_eye_filled else R.drawable.ic_eye_outlined) }
    var visualTransformation by remember (showPassword){ mutableStateOf(if (isPasswordField && !showPassword) PasswordVisualTransformation() else VisualTransformation.None) }
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholderText) },
        shape = RoundedCornerShape(16.dp),
        visualTransformation = visualTransformation,
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotEmpty(),
                enter = expandHorizontally(expandFrom = Alignment.Start),
                exit = shrinkHorizontally(shrinkTowards = Alignment.Start)
            ) {
                if (isPasswordField){
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            painter = painterResource(passwordResouce),
                            contentDescription = "Show Password"
                        )
                    }
                } else {
                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            }
        }
    )
}