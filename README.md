# 🔐 Firebase Authentication & Cloud Suite Sample App

An Android application built with **Jetpack Compose (Material 3)**, **Firebase Suite**, **Android Credential Manager API**, **Dagger Hilt**, and **Turbine Flow Testing**, following modern Android development best practices, MVVM architecture, and Clean Architecture principles.

---

## 🚀 Key Features & Capabilities

- ✉️ **Email & Password Authentication**: Full sign-up, sign-in, and password reset flows powered by Firebase Auth.
- 🌐 **Google One-Tap Sign-In**: Integration with Android's modern **Credential Manager API** (`androidx.credentials`) using `GetGoogleIdOption`.
- 🗄️ **User Profile Persistence**: Firestore auto-provisioning of user profile data (`uid`, `email`, `displayName`) upon registration.
- 💾 **Session Management**: Jetpack Preferences DataStore for asynchronous, thread-safe authentication state persistence.
- 🔔 **Push Notifications**: Firebase Cloud Messaging (FCM) integration handling Android 13+ runtime permissions (`POST_NOTIFICATIONS`) and notification channels.
- 💥 **Crashlytics & Analytics**: Automated crash reporting and event logging with in-app trigger for real-time verification.
- 🧪 **Comprehensive Unit Testing**: Declarative `StateFlow` testing using **Turbine** and **Kotlin Coroutines Test** with custom test doubles.

---

## 🌟 Architecture & Key Technologies

### 1. 🔑 Firebase Authentication & Google Credential Manager
* **Significance**: Provides secure, industry-standard identity management without custom server infrastructure.
* **Implementation**:
  - Handles email registration (`createUserWithEmailAndPassword`) and sign-in (`signInWithEmailAndPassword`).
  - Sends self-service password reset emails (`sendPasswordResetEmail`).
  - Uses `androidx.credentials` with `GetGoogleIdOption` in `SignInScreen.kt` to retrieve Google ID tokens and exchange them for Firebase credentials (`GoogleAuthProvider.getCredential`).

### 2. 🗄️ Cloud Firestore
* **Significance**: Flexible, real-time NoSQL database.
* **Implementation**:
  - Automatically provisions user profiles in the `users` collection upon registration or Google Sign-In via `AuthRepositoryImpl.saveToFirestore()`.

### 3. 💾 Jetpack Preferences DataStore
* **Significance**: Asynchronous, transactional key-value storage replacing legacy `SharedPreferences`.
* **Implementation**:
  - `DatastoreManager` persists the `authenticated` state boolean across app restarts.
  - Queried during app launch to dynamically direct users to `HomeScreen` or `SignInScreen`.

### 4. 🔔 Firebase Cloud Messaging (FCM)
* **Significance**: Cross-platform messaging service for push notifications.
* **Implementation**:
  - `MessagingService` extends `FirebaseMessagingService` to receive notifications (`onMessageReceived`) and update FCM tokens (`onNewToken`).
  - Accompanist permissions request `POST_NOTIFICATIONS` at runtime for Android 13+.

### 5. 💥 Firebase Crashlytics & Analytics
* **Significance**: Real-time crash diagnostics and usage metrics.
* **Implementation**:
  - `FirebaseModule` injects `FirebaseAnalytics` globally.
  - A test trigger button on `HomeScreen` allows forcing a runtime crash (`nullableProperty?.inc()`) to verify Crashlytics stack traces in the Firebase Console.

### 6. 💉 Dagger Hilt (Dependency Injection)
* **Significance**: Simplifies lifecycle management and decoupled architecture.
* **Implementation**:
  - `FirebaseModule` provides singleton instances (`FirebaseAuth`, `FirebaseFirestore`, `FirebaseAnalytics`, `FirebaseStorage`).
  - `RepositoryModule` binds interface contracts (`AuthRepository`, `DatastoreRepository`) to their concrete implementations.
  - ViewModels (`AuthViewModel`, `HomeViewModel`, `SettingsViewModel`) injected via `@HiltViewModel`.

### 7. 🧪 Unit Testing with Turbine & Coroutines Test
* **Significance**: Isolated, deterministic testing of asynchronous reactive flows without hitting live Firebase servers.
* **Implementation**:
  - `AuthViewModelTest` uses **Turbine** (`viewModel.authState.test { ... }`) to assert state transitions (`Loading` -> `Success` / `Error`).
  - Custom test doubles (`TestAuthRepository` and `TestDatastoreRepository`) mock success and failure conditions for:
    - `signUp()`
    - `signIn()`
    - `sendPasswordResetEmail()`
    - `signInWithGoogle()`
    - `signOut()`

---

## 📐 Project Architecture & Structure

```
com.sam.firebaseauthentication_r
├── authentication/   # Sign In, Sign Up, Password Reset UI & Auth Repository
├── components/       # Reusable Compose components & slide transition animations
├── datastore/        # Preferences DataStore manager & repository implementation
├── detail/           # Detail screen
├── di/               # Hilt Dependency Injection modules (FirebaseModule, RepositoryModule)
├── fcm/              # Firebase Messaging Service & Notification helper
├── home/             # Home UI, Home Repository, & Home ViewModel
├── navigation/       # Navigation graph, destinations, & deep link configuration
├── settings/         # App settings & session logout management
├── splash/           # Splash screen & initial auth check
├── ui/theme/         # Material 3 colors, typography, shapes, & theme definitions
└── utils/            # Constants & utility classes
```

- **Language**: Kotlin 2.x
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM + Clean Architecture / Repository Pattern
- **Reactive Stream**: Kotlin Coroutines & `StateFlow`
- **Testing**: JUnit 4, Turbine, `kotlinx-coroutines-test`

---

## 🛠️ Getting Started & Setup

### Prerequisites
* **Android Studio**: Ladybug / 2024.2+
* **JDK**: 11+
* **Android SDK**: Min SDK 26 | Target SDK 35/36

### 📋 Setup Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/Firebase-Authentication-R.git
   cd Firebase-Authentication-R
   ```

2. **Add `google-services.json`**:
   - Create a project in [Firebase Console](https://console.firebase.google.com/).
   - Download `google-services.json` and place it in the `app/` directory (`app/google-services.json`).

3. **Configure Google Sign-In SHA-1**:
   - Obtain your SHA-1 fingerprint by running:
     ```bash
     ./gradlew signingReport
     ```
   - In **Firebase Console -> Project Settings -> Your Apps**, add your SHA-1 fingerprint.
   - Re-download `google-services.json` and overwrite `app/google-services.json`.

4. **Enable Firebase Services**:
   - Go to **Authentication -> Sign-in method** and enable **Email/Password** and **Google**.
   - Enable **Firestore Database** in test mode.

---

## 🧪 Running Unit Tests

To run unit tests for `AuthViewModel` and test doubles:

* **In Android Studio**: Right-click `AuthViewModelTest.kt` and click **Run 'AuthViewModelTest'** (or click the play button next to `class AuthViewModelTest`).
* **Via Command Line**:
  ```bash
  ./gradlew testDebugUnitTest
  ```

---

## 📄 License

This project is licensed under the MIT License.
