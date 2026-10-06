package com.sam.firebaseauthentication_r.authentication

import android.R.attr.contentDescription
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sam.firebaseauthentication_r.authentication.signin.CustomTextField
import com.sam.firebaseauthentication_r.components.VerticalSpacer

@Composable
fun CompanyInfo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            modifier = modifier,
            text = "Company Name",
            style = MaterialTheme.typography.headlineLarge
        )
    }

}

@Composable
fun EmailAndPasswordContent(
    modifier: Modifier = Modifier,
    showEmailField: Boolean = true,
    showPasswordField: Boolean = true,
    email: String = "",
    password: String = "",
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onEmailClear: () -> Unit = {},
    onPasswordClear: () -> Unit = {},
    middleContent: (@Composable () -> Unit)? = null,
    actionButtonContent: @Composable () -> Unit,
    enableActionButton: Boolean = true,
    onActionButtonClick: () -> Unit,
    showConfirmPasswordField: Boolean = false,
    confirmPasswordValue: String = "",
    onConfirmPasswordChange: (String) -> Unit = {},
    onConfirmPasswordClear: () -> Unit = {}
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showEmailField) {
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = onEmailChange,
                labelText = "Email",
                placeholderText = "Enter your email",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email Icon"
                    )
                },
                onClear = onEmailClear
            )

            VerticalSpacer(size = 8)
        }

        if (showPasswordField) {
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = onPasswordChange,
                placeholderText = if (showEmailField) "Enter your password" else "Create new password",
                isPasswordField = true,
                onClear = onPasswordClear
            )
        }

        if (showConfirmPasswordField){
            VerticalSpacer(8)
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmPasswordValue,
                onValueChange = onConfirmPasswordChange,
                placeholderText = "Confirm your password",
                isPasswordField = true,
                onClear = onConfirmPasswordClear
            )
        }

        if (middleContent != null) {
            middleContent()
        } else {
            VerticalSpacer(size = 16)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onActionButtonClick,
            enabled = enableActionButton
        ) {
            actionButtonContent()
        }
    }

}