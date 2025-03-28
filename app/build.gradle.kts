plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")}

android {

    signingConfigs {
        getByName("debug") {
            storeFile = file(rootProject.extra["myValue"] as String)
            storePassword = "123456"
            keyAlias = "compose"
            keyPassword = "123456"
        }
    }
    namespace = "com.ndroid.jetpackcomposepractice"
    compileSdk = 35

    defaultConfig {

        applicationId = "com.ndroid.jetpackcomposepractice"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)
    implementation (libs.androidx.runtime.livedata)
    implementation(libs.coil.compose)

//    navigation
    implementation (libs.androidx.navigation.compose)

//    Serialization
    implementation(libs.kotlinx.serialization.json)


    implementation(libs.okhttp)
    implementation(libs.opencv)

    implementation (libs.play.services.mlkit.document.scanner)


}