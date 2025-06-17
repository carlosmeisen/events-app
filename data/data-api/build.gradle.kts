plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.data.api"
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
    // Retrofit & OkHttp
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)

    // Moshi
    implementation(libs.moshi.core)
    implementation(libs.moshi.kotlin)
    // kapt(libs.moshi.kotlin.codegen) // Uncomment if using KAPT/KSP and add 'kotlin-kapt' plugin

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(project(":domain:domain-models"))

    // Dependencies on domain-model

    // Testing (for mocking API responses)
    testImplementation(project(":core:core-testing"))
}