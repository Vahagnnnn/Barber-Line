plugins {
    id("com.android.application") version "8.8.0"
    id("com.google.gms.google-services") version "4.4.2"
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}


android {
    namespace = "com.vahagn.barber_line"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vahagn.barber_line"
        minSdk = 24
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation (libs.ccp)
    implementation (libs.libphonenumber)
    implementation (libs.volley)
    implementation (libs.recyclerview.v121)
    implementation(libs.play.services.maps.v1820)
    implementation(libs.com.google.android.libraries.places.places)
    implementation(libs.recyclerview.v132)
    implementation(libs.github.glide)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.recyclerview)
    annotationProcessor(libs.compiler.v4120)
    implementation(libs.firebase.auth.v2320)
    implementation(libs.google.firebase.analytics)
    implementation(libs.firebase.database)
    implementation(libs.imagepicker)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.storage)
    implementation(libs.ucrop)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.play.services.maps.v1900)

    implementation (libs.places.v330)
    implementation (libs.play.services.maps.v1920)
    implementation (libs.okhttp)
}


