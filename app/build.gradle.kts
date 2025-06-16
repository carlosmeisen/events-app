plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.festiveapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.festiveapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            // Debug build type is often configured by default by AGP.
            // isDebuggable = true (default)
            // You can add custom fields or overrides here if needed.
            applicationIdSuffix = ".debug" // Optional: e.g., com.example.festiveapp.debug
            versionNameSuffix = "-debug"   // Optional: e.g., 1.0-debug
            buildConfigField("String", "API_ENDPOINT", "\"https://debug.api.example.com/\"")
            buildConfigField("boolean", "FEATURE_FLAG_NEW_UI", "true")
            // For debug, you might want to explicitly state that it's not minified (though it's default)
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_ENDPOINT", "\"https://api.example.com/\"")
            buildConfigField("boolean", "FEATURE_FLAG_NEW_UI", "false")
        }
        create("staging") {
            // It's good practice to initialize from another build type
            // if they share many configurations, then override.
            initWith(getByName("debug")) // Inherits settings from debug, like isDebuggable = true

            applicationIdSuffix = ".staging" // e.g., com.example.festiveapp.staging
            versionNameSuffix = "-staging"   // e.g., 1.0-staging

            // Override specific settings for staging
            buildConfigField("String", "API_ENDPOINT", "\"https://staging.api.example.com/\"")
            buildConfigField("boolean", "FEATURE_FLAG_NEW_UI", "true") // Example: testing new UI on staging
            // Staging might not be minified, or might use different ProGuard rules
            isMinifyEnabled = false
            // You might want specific signing configs for staging if you distribute it
            // signingConfig = signingConfigs.getByName("debug") // Or a custom staging signingConfig
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerExtension.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM - CRITICAL: This manages all Compose library versions
    implementation(platform(libs.androidx.compose.bom))  // Add platform() wrapper
    implementation(libs.bundles.compose.base)
    debugImplementation(libs.bundles.compose.debug)

    // Navigation - IMPORTANT: These need to be compatible with Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // DataStore
    implementation(libs.bundles.datastore)

    implementation(libs.androidx.lifecycle.runtime.compose)

    // Project modules
    implementation(project(":core:core-navigation"))
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-logging"))
    implementation(project(":domain:domain-usecase"))
    implementation(project(":domain:domain-repository"))
    implementation(project(":domain:domain-model"))
    implementation(project(":data:data-repository"))
    implementation(project(":navigation:navigation-engine"))
    implementation(project(":navigation:navigation-ui"))
    implementation(project(":infrastructure"))
    implementation(project(":mapper"))
    implementation(project(":analytics:logging"))


    // Feature modules
    implementation(project(":feature:feature-home"))
    implementation(project(":feature:feature-settings"))
    implementation(project(":feature:feature-calendar"))
    implementation(project(":feature:feature-favorites"))
    implementation(project(":feature:feature-login"))

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.ext)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))  // BOM for tests too
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}