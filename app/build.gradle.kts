plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.checkid"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.checkid"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    //
    viewBinding  {
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.runtime.saved.instance.state)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.preference.ktx)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Fragment 관련 dependency
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.fragment.ktx)

    // BottomAppBar 관련 dependency
    implementation(libs.androidx.material3)

    //MPAndroidChart 관련 dependency
    implementation(libs.mpandroidchart)

    // Google Maps 관련 dependency
    // implementation(libs.google.maps)
    implementation("androidx.preference:preference:1.2.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    // FireBase 관련 dependency
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.database)
}

// build.gradle.kts (프로젝트 레벨)

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}



