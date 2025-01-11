plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) // Kotlin Plugin
    alias(libs.plugins.kotlin.kapt)   // Für Annotation Processing
}

android {
    namespace = "com.example.Wizard_Helper_v2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.Wizard_Helper_v2"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17" // Maximale unterstützte Kotlin JVM-Zielversion
    }


    buildFeatures {
        viewBinding = true
    }
}
dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Android-Test-Abhängigkeiten
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // JUnit 5 (lokale Tests)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)

    // Room Abhängigkeiten
    implementation(libs.room.runtime.v261)
    kapt(libs.room.compiler.v261)
    implementation(libs.room.ktx.v261)

    implementation("com.google.code.gson:gson:2.10.1") // Aktuelle Version
}

tasks.withType<Test> {
    useJUnitPlatform()
}
