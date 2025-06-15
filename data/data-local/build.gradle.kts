plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // id("kotlin-kapt") // Uncomment if using Room with annotation processing
}

android {
    namespace = "com.example.data.local"
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
    // Room Database (Example)
    // implementation(libs.room.runtime)
    // kapt(libs.room.compiler)
    // implementation(libs.room.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Dependencies on domain-model
    implementation(project(":domain:domain-model"))

    // Testing
    testImplementation(project(":core:core-testing"))
}