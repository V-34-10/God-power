plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.divinee.puwer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.divinee.puwer"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.exoplayer)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //Adjust SDK
    implementation(libs.adjust.adjust.android)
    implementation(libs.adjust.android.webbridge)
    implementation(libs.adjust.android.samsung.referrer)
    implementation(libs.installreferrer.samsung.galaxystore.install.referrer)

    //Google Play Services
    implementation(libs.play.services.ads.identifier)

    // AppMetrica SDK.
    implementation(libs.analytics)

    //Advertiser ID
    implementation(libs.androidx.ads.identifier)
    implementation(libs.guava)

    //Install Referrer
    implementation(libs.installreferrer)

    //Firebase
    implementation(libs.firebase.components)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.analytics)

    // Okhttp3
    implementation(libs.okhttp)
    implementation(libs.okhttp3.integration)

    // JSON
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
}