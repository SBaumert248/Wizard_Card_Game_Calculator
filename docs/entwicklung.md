# Entwicklung

## Voraussetzungen

- Android Studio mit Android SDK 34
- JDK 17
- Gradle über den mitgelieferten Wrapper

Die App unterstützt Geräte ab API 26 und zielt auf API 34. Java- und
Kotlin-Bytecode werden für JVM 17 erzeugt.

## Projekt öffnen

Das Repository als bestehendes Android-Studio-Projekt öffnen und die
Gradle-Synchronisierung abwarten. Das einzige Anwendungsmodul heißt `app`.

## Build und Tests

Unter Windows:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat test
.\gradlew.bat connectedAndroidTest
```

Unter Linux oder macOS:

```bash
./gradlew assembleDebug
./gradlew test
./gradlew connectedAndroidTest
```

`connectedAndroidTest` benötigt einen laufenden Emulator oder ein verbundenes
Gerät. Falls Gradle Java nicht findet, muss `JAVA_HOME` auf eine JDK-17-
Installation zeigen.

## Wichtige Konfiguration

| Datei | Zweck |
| --- | --- |
| `settings.gradle.kts` | Projektname, Modul und Repositories |
| `build.gradle.kts` | Android-Plugin auf Projektebene |
| `gradle/libs.versions.toml` | zentrale Versions- und Dependency-Aliase |
| `app/build.gradle.kts` | Android-SDKs, Java/Kotlin-Ziel, Dependencies und Tests |
| `app/src/main/AndroidManifest.xml` | Launcher-Activity, Theme und Backup-Einstellungen |

Wesentliche Bibliotheken:

- AndroidX AppCompat, ConstraintLayout und Navigation
- Material Components
- Gson für Spielstand-JSON
- JUnit Jupiter für lokale Tests
- AndroidX Test und Espresso für instrumentierte Tests

Room ist als Dependency konfiguriert, wird im Quellcode aktuell aber nicht
eingesetzt.

## Quellstruktur

```text
app/src/main/
├── java/com/example/Wizard_Helper_v2/
│   ├── Controller/WizardGame.java
│   ├── Model/
│   │   ├── Points.java
│   │   └── AppState.java
│   └── View/
│       ├── MainActivity.java
│       ├── FirstFragment.java
│       └── SecondFragment.java
├── res/
│   ├── layout/
│   ├── navigation/
│   ├── menu/
│   ├── values*/
│   └── drawable/ und mipmap-*/
└── AndroidManifest.xml
```

Lokale Unit-Tests liegen unter `app/src/test`, instrumentierte Tests unter
`app/src/androidTest`.

## Änderungshinweise

### Spielregeln

Änderungen an der Punkteformel gehören in `Model/Points.java`. Die zulässige
Summe der tatsächlich erzielten Stiche wird dagegen im `FirstFragment`
geprüft. Beide Bereiche sollten bei Regeländerungen gemeinsam getestet werden.

### Spielerzahl oder Rundenzahl

Die Zuordnung steht in `WizardGame.startGame()`; die umgekehrte Zuordnung steht
separat in `WizardGame.numOfPlayer()`. UI und Ressourcen sind derzeit fest auf
maximal sechs Spieler ausgelegt.

### Neue Einstellung

Eine neue Einstellung benötigt derzeit

1. ein Steuerelement in `fragment_second.xml`,
2. Lesen und Schreiben in `SecondFragment`,
3. Auswertung an der verwendenden Stelle und
4. einen Text in `strings.xml`.

### Persistenzformat

Gson serialisiert die internen Felder von `WizardGame` und `Points` direkt.
Umbenennungen oder Typänderungen können bestehende `game.json`-Dateien
inkompatibel machen. Für produktive Migrationen sollte ein versioniertes
Persistenzmodell eingeführt werden.

## Paket und Release

Namespace und Application-ID lauten aktuell
`com.example.Wizard_Helper_v2`. Vor einer Veröffentlichung sollte insbesondere
geprüft werden, ob diese Beispiel-Domain dauerhaft verwendet werden soll.
Release-Minifizierung ist deaktiviert und `versionCode` steht auf 1,
`versionName` auf 1.1.

