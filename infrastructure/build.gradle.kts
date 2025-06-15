plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.infrastructure"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
// Dependency on the core-navigation module for the interface
    implementation(project(path = ":core:core-navigation"))
    implementation(project(path = ":core:core-ui"))
    implementation(project(path = ":domain:domain-usecase"))
    implementation(project(path = ":domain:domain-model"))
    implementation(project(path = ":mapper"))

    // AndroidX Core for ContextCompat (needed for startActivity)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)

    // Koin for dependency injection
    implementation(libs.koin.android)

    implementation(libs.kotlinx.coroutines.core)

    // Testing dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test) // If you have suspend functions in the future
    // For Android-specific unit tests (like testing intents) - Robolectric can be useful here
    // testImplementation("org.robolectric:robolectric:4.10.3") // Example Robolectric dependency
}