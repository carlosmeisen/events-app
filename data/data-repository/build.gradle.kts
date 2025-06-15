plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.data.repository"
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
    // Dependency on the domain module for the repository interface
    implementation(project(path = ":domain:domain-repository"))
    implementation(project(path = ":domain:domain-model"))

    // Dependency on the data-local module for the data store implementation
    implementation(project(path = ":data:data-local")) // Assuming ThemeDataStore is in data-local

    // Koin for dependency injection
    implementation(libs.koin.android)

    // Kotlin Coroutines Core (if your repository uses coroutines directly)
    implementation(libs.kotlinx.coroutines.core)

    // For DataStore dependencies that ThemePreferenceRepositoryImpl might need
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)

    // Testing dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}