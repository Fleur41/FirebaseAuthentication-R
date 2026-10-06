package com.sam.firebaseauthentication_r.authentication

class TestAuthRepository: AuthRepository {
    var signUpError: Boolean = false
    override fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: () -> Unit,
        onSignUpFailure: (Exception) -> Unit
    ) {
        println("Signing up with email: $email, password: $password")
        if (signUpError) {
            onSignUpFailure(RuntimeException("Sign up failed"))
            return
        }
        onSignUpSuccess()
    }

    var signInError: Boolean = false
    override fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        println("Signing in with email: $email, password: $password")
        if (signInError){
            onFailure(RuntimeException("Sign in failed"))
            return
        }
        onSuccess()
    }

    var sendPasswordResetEmailError: Boolean = false
    override fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        println("Sending password reset email to: $email")
        if(sendPasswordResetEmailError){
            onFailure(RuntimeException("Failed to send reset email"))
            return
        }
        onSuccess()
    }

    var signInWithGoogleError: Boolean = false
    override fun signInWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        println("Signing in with Google token: $idToken")
        if (signInWithGoogleError){
            onFailure(RuntimeException("Google Sign-In failed"))
            return
        }
        onSuccess()
    }

    var isSignedOut: Boolean = false
    override fun signOut() {
        println("Sign out")
        isSignedOut = true
    }
}