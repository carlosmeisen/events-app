plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.core.ui"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true // Enables Compose features for this module
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerExtension.get()
    }
    // This packaging block is often needed in Compose library modules
    // to prevent conflicts with META-INF files during APK generation.
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // AndroidX & Compose Core
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom)) // Ensures all Compose libs use compatible versions

    implementation(libs.bundles.compose.base)

    // Specific dependency for Downloadable Google Fonts
    implementation(libs.androidx.compose.ui.text.googleFonts)

    // Debugging Compose (UI Tooling Preview, etc.)
    debugImplementation(libs.bundles.compose.debug)
    debugImplementation(libs.androidx.compose.ui.tooling) // Explicitly add tooling if not in compose.debug

    // Testing dependencies for UI modules
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM for Compose UI testing
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest) // For Android Compose UI tests
}