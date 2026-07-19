import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.android.application)
    jacoco
}

android {
    namespace = "de.sbaumert.wizardhelper"
    compileSdk = 35

    defaultConfig {
        applicationId = "de.sbaumert.wizardhelper"
        minSdk = 26
        targetSdk = 35
        versionCode = 4
        versionName = "1.3.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    androidTestImplementation(libs.test.core)

    // JUnit 5 (lokale Tests)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    implementation("com.google.code.gson:gson:2.14.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.14"
}

val coverageExclusions = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/databinding/**"
)

tasks.register<JacocoReport>("jacocoTestReport") {
    group = "verification"
    description = "Generates HTML and XML coverage reports for debug unit tests."

    dependsOn("testDebugUnitTest")

    executionData.setFrom(
        layout.buildDirectory.file(
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
        )
    )
    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(
        fileTree(
            layout.buildDirectory.dir(
                "intermediates/javac/debug/compileDebugJavaWithJavac/classes"
            )
        ) {
            exclude(coverageExclusions)
        }
    )

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }
}

tasks.register<JacocoReport>("jacocoCombinedTestReport") {
    group = "verification"
    description = "Generates a combined HTML and XML coverage report for debug unit and UI tests."

    dependsOn("testDebugUnitTest", "connectedDebugAndroidTest")

    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include(
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "outputs/code_coverage/debugAndroidTest/connected/**/*.ec"
            )
        }
    )
    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(
        fileTree(
            layout.buildDirectory.dir(
                "intermediates/javac/debug/compileDebugJavaWithJavac/classes"
            )
        ) {
            exclude(coverageExclusions)
        }
    )

    reports {
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/jacocoCombinedTestReport/html"))
        xml.required.set(true)
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/jacocoCombinedTestReport/jacocoCombinedTestReport.xml"))
        csv.required.set(false)
    }
}
