# 🔐 Firebase Authentication & Services Sample App

An Android application built with **Jetpack Compose**, **Firebase Suite**, **Android Credential Manager API**, and **Dagger Hilt**, following modern Android development best practices and Clean Architecture principles.

---

## 🌟 Significance & Usage of Key Technologies

### 1. 🔑 Firebase Authentication (Email/Password & Google Sign-In)
* **Significance**: Provides robust, industry-standard identity management without requiring custom backend servers or password hashing infrastructure.
* **How it's used**:
  - Handles secure registration (`createUserWithEmailAndPassword`) and sign-in (`signInWithEmailAndPassword`).
  - Delivers self-service password reset emails (`sendPasswordResetEmail`).
  - Integrates with Android **Credential Manager API** (`GetGoogleIdOption`) to convert Google ID tokens into Firebase credentials (`GoogleAuthProvider.getCredential`).

### 2. 🗄️ Cloud Firestore
* **Significance**: A flexible, scalable NoSQL cloud database that enables real-time data synchronization across client devices.
* **How it's used**:
  - Automatically checks and creates user profile records (`uid`, `email`, `displayName`) in the `users` document collection upon registration or Google Sign-In inside `AuthRepositoryImpl.saveToFirestore()`.

### 3. 🔔 Firebase Cloud Messaging (FCM)
* **Significance**: Provides reliable, battery-efficient cross-platform messaging to send push notifications and keep users engaged.
* **How it's used**:
  - `MessagingService` extends `FirebaseMessagingService` to receive remote messages (`onMessageReceived`) and retrieve FCM tokens (`onNewToken`).
  - Handles Android 13+ runtime notification permissions (`POST_NOTIFICATIONS`) via Accompanist and posts notifications using local `NotificationChannel`s.

### 4. 💥 Firebase Crashlytics
* **Significance**: Delivers real-time crash reporting and stack traces, allowing developers to identify, prioritize, and fix app stability issues before they impact users.
* **How it's used**:
  - Captures unhandled runtime exceptions automatically.
  - Includes a test button on `HomeScreen` (`nullableProperty?.inc()`) to intentionally trigger a crash and verify reporting in the Firebase Console.

### 5. 📊 Firebase Analytics
* **Significance**: Gives actionable insights into app adoption, user engagement, and navigation patterns.
* **How it's used**:
  - Initialized globally via Dagger Hilt in `FirebaseModule` to automatically log screen transitions, session starts, and core user interactions.

### 6. 📂 Jetpack Preferences DataStore
* **Significance**: A modern, asynchronous key-value storage solution replacing legacy `SharedPreferences`, offering transactional safety and built-in Kotlin Coroutines / Flow support.
* **How it's used**:
  - `DatastoreManager` persists the `authenticated` state boolean.
  - Read by `SettingsViewModel` on launch to determine whether the app should navigate to `Home` or `SignIn`.

### 7. 💉 Dagger Hilt (Dependency Injection)
* **Significance**: Decouples application components, minimizes boilerplate, and simplifies unit testing by managing object lifecycles automatically.
* **How it's used**:
  - `FirebaseModule` provides singletons (`FirebaseAuth`, `FirebaseFirestore`, `FirebaseAnalytics`, `FirebaseStorage`).
  - `RepositoryModule` binds interfaces (`AuthRepository`, `DatastoreRepository`) to their concrete implementations.
  - Injected directly into ViewModels (`AuthViewModel`, `HomeViewModel`, `SettingsViewModel`) using `@HiltViewModel` and `@Inject`.

### 8. 🛡️ Android Credential Manager API (`androidx.credentials`)
* **Significance**: Google's latest unified identity framework that replaces legacy Google Sign-In and One Tap SDKs for maximum security and seamless UX.
* **How it's used**:
  - Initiates Google Sign-In using `GetGoogleIdOption` in `SignInScreen.kt`, retrieving Google ID Tokens safely without hardcoding user credentials.

---

## 🛠️ Project Architecture & Structure

```
com.sam.firebaseauthentication_r
├── authentication/   # Sign In, Sign Up, Forgot Password UI & Auth Repository
├── components/       # Reusable Compose UI components & slide animations
├── datastore/        # Preferences DataStore for session state persistence
├── detail/           # Detail screen
├── di/               # Hilt DI modules (FirebaseModule, RepositoryModule)
├── fcm/              # Firebase Messaging Service & Notification Manager
├── home/             # Home screen, Repository, & ViewModel
├── navigation/       # Navigation graph & Destinations
├── settings/         # App settings & Logout state management
├── splash/           # Splash screen
├── ui/theme/         # Material 3 colors, typography, & theme definitions
└── utils/            # Constants & helpers
```

* **Language**: [Kotlin](https://kotlinlang.org/)
* **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Architecture**: MVVM + Clean Repository Pattern
* **Concurrency**: Kotlin Coroutines & `StateFlow`
* **Dependency Injection**: Dagger Hilt

---

## 🚀 Getting Started & Setup

### Prerequisites
* **Android Studio**: Ladybug / 2024.2+
* **JDK**: 11+
* **Android SDK**: Min SDK 26 | Target SDK 37

### 📋 Configuration Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/Firebase-Authentication-R.git
   cd Firebase-Authentication-R
   ```

2. **Add `google-services.json`**:
   - Create a project in [Firebase Console](https://console.firebase.google.com/).
   - Download `google-services.json` and place it in the `app/` directory (`app/google-services.json`).

3. **Register SHA-1 Fingerprint for Google Sign-In**:
   - Run `./gradlew signingReport` or check your debug keystore to get your SHA-1.
   - In **Firebase Console -> Project Settings -> Your Apps**, click **Add fingerprint** and paste your SHA-1 fingerprint.
   - Download the updated `google-services.json` into `app/`.

4. **Enable Auth Providers**:
   - In Firebase Console, go to **Authentication -> Sign-in method** and enable **Email/Password** and **Google**.

---

## 🧪 Testing Features

* **Push Notifications**: Accept notification permissions on the Home Screen and send a test message from **Firebase Console -> Messaging**.
* **Crashlytics Verification**: Tap the **"Crash the app"** button on the Home Screen to verify real-time crash logs in **Firebase Console -> Crashlytics**.

---

## 📄 License

This project is licensed under the MIT License.
