plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    // google services
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.sam.firebaseauthentication_r"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.sam.firebaseauthentication_r"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
//    google services
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth) //firebase-auth
    implementation(libs.firebase.firestore) //firebase-firestore
    implementation(libs.firebase.analytics) //firebase-analytics
    implementation(libs.firebase.crashlytics) //firebase-crashlytics
    implementation(libs.firebase.messaging) //firebase-messaging
    implementation(libs.firebase.storage)
    // Credential Manager
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    // accompanist
    implementation(libs.accompanist.permissions.v0373)
    // Navigation
    implementation(libs.androidx.navigation.compose)
    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)
    // Material 3
    // Adds the most commonly used material icons (like Clear, ArrowBack, Share, etc.)
    implementation(libs.androidx.compose.material.icons.core)
    // Turbine testing
    testImplementation(libs.turbine)
    // RunTest
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test: 1.10.1")
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}