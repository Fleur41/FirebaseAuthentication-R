package com.sam.firebaseauthentication_r.authentication

import app.cash.turbine.test
import com.sam.firebaseauthentication_r.authentication.signup.AuthState
import com.sam.firebaseauthentication_r.authentication.signup.AuthViewModel
import com.sam.firebaseauthentication_r.datastore.DatastoreRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {
    private lateinit var viewModel: AuthViewModel
    private lateinit var authRepository: TestAuthRepository
    private lateinit var datastoreRepository: DatastoreRepository



    @Before
    fun setup() {
        authRepository = TestAuthRepository()
        datastoreRepository = TestDatastoreRepository()
        viewModel = AuthViewModel(authRepository, datastoreRepository)
    }

    @Test
    fun testSignUpSuccess(){
        runTest{
            viewModel.authState.test {
                viewModel.signUp("email@example.com", "password")
                skipItems(1) //Skip AuthState.Loading
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)
                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Success)

            }
        }

    }

    @Test
    fun testSignUpFailure(){
        authRepository.signUpError = true
        runTest{
            viewModel.authState.test {
                viewModel.signUp("email@example.com", "password")
                skipItems(1)
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)
                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Error)

            }
        }

    }

    @Test
    fun testSignInSuccess(){
        runTest{
            viewModel.authState.test {
                viewModel.signIn("email@example.com", "password")
                skipItems(1) //Skip AuthState.Loading
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)
                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Success)

            }
        }

    }

    @Test
    fun testSignInFailure(){
        authRepository.signInError = true
        runTest{
            viewModel.authState.test {
                viewModel.signIn("email@example.com", "password")
                skipItems(1)
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)
                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Error)

            }
        }

    }

    // --- Password Reset Tests ---

    @Test
    fun testSendPasswordResetEmailSuccess() {
        runTest {
            viewModel.authState.test {
                viewModel.sendPasswordResetEmail("email@example.com")
                skipItems(1) // Skip AuthState.Initial
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)
                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Success)
            }
        }
    }

    @Test
    fun testSendPasswordResetEmailFailure() {
        authRepository.sendPasswordResetEmailError = true
        runTest {
            viewModel.authState.test {
                viewModel.sendPasswordResetEmail("email@example.com")
                skipItems(1) // Skip AuthState.Initial
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)
                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Error)
            }
        }
    }

    // --- Google Sign-In Tests ---

    @Test
    fun testSignInWithGoogleSuccess() {
        runTest {
            viewModel.authState.test {
                viewModel.signInWithGoogle("mock_id_token")
                skipItems(1) // Skip AuthState.Initial
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)
                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Success)
            }
        }
    }

    @Test
    fun testSignInWithGoogleFailure() {
        authRepository.signInWithGoogleError = true
        runTest {
            viewModel.authState.test {
                viewModel.signInWithGoogle("mock_id_token")
                skipItems(1) // Skip AuthState.Initial
                val item = awaitItem()
                println("item: $item")
                Assert.assertTrue(item is AuthState.Loading)
                val nextItem = awaitItem()
                Assert.assertTrue(nextItem is AuthState.Error)
            }
        }
    }

    @Test
    fun testSignOut() {
        runTest {
            viewModel.signOut()
            Assert.assertTrue(authRepository.isSignedOut)
        }
    }
}