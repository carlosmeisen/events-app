plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.domain.usecase"
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
    implementation(project(":domain:domain-repository"))
    implementation(project(":domain:domain-model"))
    implementation(libs.kotlinx.coroutines.core)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    testImplementation(project(":core:core-testing"))
}