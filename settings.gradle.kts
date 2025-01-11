pluginManagement {
    repositories {
        google() // Für Android-spezifische Plugins
        mavenCentral() // Standard-Maven-Repository
        gradlePluginPortal() // Für Gradle-Plugins
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Erlaubt nur zentrale Repositories
    repositories {
        google() // Android-spezifische Abhängigkeiten
        mavenCentral() // Standard-Java/Kotlin-Abhängigkeiten
    }
}

rootProject.name = "Wizard Helper"
include(":app")

