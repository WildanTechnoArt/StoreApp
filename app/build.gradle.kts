plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.wildan.storeapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wildan.storeapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        setProperty("archivesBaseName", "StoreApp-v$versionCode($versionName) ")
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.paging)
    implementation (libs.androidx.room.paging)
    implementation (libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.github.razir.progressbutton)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.vectordrawable)
    implementation(libs.github.bumptech.glide)
    implementation(libs.androidx.activity)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.com.facebook.shimmer)
    ksp(libs.github.bumptech.ksp)
    implementation(libs.hilt.android) // Hilt dependencies
    implementation (libs.hilt.android.testing)
    ksp(libs.hilt.android.compiler) // Hilt annotation processor
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.com.squareup.retrofit)
    implementation(libs.com.squareup.gson)
    implementation(libs.com.squareup.rxjava2)
    implementation(libs.org.jetbrains.serialization)
    implementation(libs.google.code.gson)
    implementation(AndroidDependencies.HTTP_LOG)
    testImplementation(libs.junit)
    debugImplementation (libs.leakcanary.android)
    androidTestImplementation(libs.androidx.junit)
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.inline)

    // Hilt testing dependencies
    testImplementation (libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.androidx.core.testing)

    androidTestImplementation(libs.androidx.espresso.core)
}