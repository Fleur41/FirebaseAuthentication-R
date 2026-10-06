package com.sam.firebaseauthentication_r.authentication.signin


import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.sam.firebaseauthentication_r.R
import com.sam.firebaseauthentication_r.authentication.CompanyInfo
import com.sam.firebaseauthentication_r.authentication.EmailAndPasswordContent
import com.sam.firebaseauthentication_r.authentication.signup.AuthState
import com.sam.firebaseauthentication_r.authentication.signup.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()
    val webClientId = "183617969557-o26ds6fdtoljq4eq1l909aknb2up40ni.apps.googleusercontent.com"

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            // We don't need to explicitly to navigate to HomeScreen as it will be taken core of by statFlow in SettingsViewModel
        }
    }

    fun launchGoogleSignIn() {
        val activity = context as? androidx.activity.ComponentActivity ?: return
        val credentialManager = CredentialManager.create(activity)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutine.launch {
            try {
                val result = credentialManager.getCredential(activity, request)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    authViewModel.signInWithGoogle(googleIdTokenCredential.idToken)
                }
            } catch (e: GetCredentialCancellationException) {
                Log.w("TAG", "Google Sign-In canceled or re-auth failed: ", e)
                if (e.message?.contains("16") == true) {
                    Toast.makeText(context, "Sign-In Failed: Add SHA-1 to Firebase Console", Toast.LENGTH_LONG).show()
                }
            } catch (e: GetCredentialException) {
                Log.e("TAG", "Google Sign-In Error: ", e)
                Toast.makeText(context, "Google Sign-In Error: ${e.message}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("TAG", "Google Sign-In Error: ", e)
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
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
            CompanyInfo(modifier = Modifier.weight(.5f))


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
                    middleContent = {
                        Text(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 16.dp, bottom = 16.dp, end = 4.dp)
                                .clickable { onForgotPasswordClick() },
                            text = "Forgot Password?",
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        )
                    },
                    onActionButtonClick = {
                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(
                                context,
                                "Please enter email/password",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@EmailAndPasswordContent
                        }
                        authViewModel.signIn(email.trim(), password.trim())
                    }
                )

                Column(
                    modifier = Modifier.weight(1.2f),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OrDivider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))

                        Spacer(modifier = Modifier.height(8.dp))
                        GoogleSignInButton(
                            onGoogleClick = { launchGoogleSignIn() }
                        )

                        Box(modifier = Modifier.padding(top = 8.dp)) {
                            if (authState is AuthState.Error) {
                                Text(
                                    text = (authState as AuthState.Error).message,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
                SignUpBox(
                    modifier = Modifier.weight(.6f),
                    onSignUpClick = onSignUpClick
                )
            }
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
        Row{
            Text(
                text = "Don't have an account?",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier.clickable{onSignUpClick()},
                text = "Sign up instead",
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
    labelText: String? = null,
    placeholderText: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    onClear: () -> Unit,
    isPasswordField: Boolean = false
) {
    var showPassword by remember { mutableStateOf(false) }
    var passwordResource by remember (showPassword){ mutableIntStateOf(if (showPassword) R.drawable.ic_eye_filled else R.drawable.ic_eye_outlined) }
    var visualTransformation by remember (showPassword){ mutableStateOf(if (isPasswordField && !showPassword) PasswordVisualTransformation() else VisualTransformation.None) }
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = if (labelText != null) { { Text(text = labelText) } } else null,
        leadingIcon = leadingIcon,
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
                            painter = painterResource(passwordResource),
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

@Composable
fun OrDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray
        )
        Text(
            text = "OR",
            modifier = Modifier.padding(horizontal = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray
        )
    }
}

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    onGoogleClick: () -> Unit
) {
    Button(
        onClick = onGoogleClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFB800)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Sign in with Google",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}