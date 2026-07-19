# Sicherheits- und Risikobericht

Stand: 19. Juli 2026

## Umfang

Geprüft wurden Build-Konfiguration, direkte Abhängigkeiten, Android-Manifest,
Backup-Regeln, lokale Persistenz, Eingabevalidierung, Zustandslogik und
vorhandene Tests. Die App fordert keine Netzwerk- oder sonstigen gefährlichen
Android-Berechtigungen an.

## Behobene Risiken

| Priorität | Risiko | Änderung |
| --- | --- | --- |
| Hoch | Beschädigtes oder manipuliertes JSON konnte Parser-, Null- oder Indexfehler beim App-Start verursachen | Striktes UTF-8-Parsing, Abfangen von Parserfehlern und vollständige Struktur-/Werteprüfung vor Zustandsübernahme |
| Hoch | Negative oder zu große Rundenindizes konnten Arrayzugriffe außerhalb der Grenzen auslösen | Einheitliche Prüfung `0 <= round < length`; Regressionstests ergänzt |
| Mittel | Direktes Überschreiben von `game.json` konnte bei Prozessabbruch eine teilweise Datei hinterlassen | Schreiben in temporäre Datei, Flush/Sync und anschließendes atomares Ersetzen |
| Mittel | Spielernamen und Spielstände waren für Android-Backup bzw. Gerätetransfer freigegeben | `allowBackup=false` sowie explizite Ausschlüsse in beiden Backup-Regelwerken |
| Mittel | Unplausible Trickwerte und Summenüberlauf waren möglich | Einzelwerte auf `0..Rundenzahl` begrenzt; Summenbildung auf `long` umgestellt |
| Mittel | Ungültige Spielerzahlen markierten ein Spiel trotzdem als laufend | Spielstatus wird nur noch für 3 bis 6 Spieler aktiviert |
| Mittel | Veraltete direkte Dependencies und Build-Werkzeuge | Stabile Updates für AGP, Gradle, AndroidX, Material, Gson und Testbibliotheken |
| Mittel | Gradle-Distribution war nicht kryptografisch im Projekt fixiert | Offizielle SHA-256-Prüfsumme für Gradle 8.13 im Wrapper hinterlegt |
| Niedrig | Nicht verwendete Room-, Kotlin- und KAPT-Komponenten vergrößerten Build- und Abhängigkeitsfläche | Plugins, Dependencies, Versionsaliase und ungenutzte `AppState`-Klasse entfernt |
| Niedrig | JUnit Engine und der von Gradle geladene Platform Launcher waren nicht versionsgleich | JUnit Platform Launcher 1.13.4 explizit als Test-Runtime eingebunden |
| Niedrig | Nicht-positionale Ressourcenplatzhalter und fest kodierte Rechtsausrichtung verursachten Lint-Befunde | Platzhalter positioniert und Layoutattribute RTL-kompatibel gemacht |

## Aktualisierte Versionen

| Komponente | Vorher | Nachher |
| --- | ---: | ---: |
| Android Gradle Plugin | 8.7.3 | 8.11.2 |
| Gradle Wrapper | 8.11.1 | 8.13 |
| compileSdk / targetSdk | 34 | 35 |
| AppCompat | 1.7.0 | 1.7.1 |
| Material Components | 1.12.0 | 1.14.0 |
| ConstraintLayout | 2.2.0 | 2.2.1 |
| Navigation | 2.8.5 | 2.9.8 |
| Gson | 2.10.1 | 2.14.0 |
| JUnit Jupiter | 5.10.0 | 5.13.4 |
| AndroidX Test JUnit | 1.2.1 | 1.3.0 |
| Espresso | 3.6.1 | 3.7.0 |

Es wurden stabile statt Alpha-/Beta-Versionen gewählt. Ein Wechsel auf AGP 9
ist eine eigene Migration und wurde nicht mit den Sicherheitsfixes vermischt.

## Prüfnachweise

- Gradle Wrapper 8.13 erfolgreich heruntergeladen und gestartet
- `Points.java`, `WizardGame.java` und die lokalen Tests erfolgreich mit
  JDK 17 kompiliert
- 28 von 28 lokalen Unit-Tests in Debug und Release erfolgreich
- Android Lint ohne Fehler abgeschlossen
- Debug-APK erfolgreich gebaut
- `git diff --check` als Abschlussprüfung vorgesehen
- zusätzliche Unit-Tests für negative Indizes, unzulässige Trickwerte,
  ungültige Spielerzahlen und beschädigtes JSON ergänzt

Der vollständige Task `test lintDebug assembleDebug` wurde nach Installation
von Android SDK 35 erfolgreich ausgeführt. Lint enthält noch nicht blockierende
Warnungen, insbesondere zu Barrierefreiheit und ungenutzten Ressourcen.

## Verbleibende Risiken

1. Das JSON-Format besitzt noch keine explizite Schemaversion oder Migration.
2. Die UI informiert nicht darüber, wenn ein gespeicherter Spielstand
   verworfen wurde.
3. Die Wiederherstellung einer laufenden Partie ist noch nicht durch
   instrumentierte UI-Tests abgedeckt.
4. Android-Resource-IDs werden als fachliche Spieler-IDs verwendet.
5. Release-Minifizierung ist deaktiviert; eine kontrollierte R8-Aktivierung
   sollte separat mit Persistenz- und UI-Tests erfolgen.
6. Material Components und die fragmentbasierte Navigation befinden sich
   inzwischen im Wartungsmodus. Eine Compose-Migration ist strategisch
   sinnvoll, aber kein kleiner Sicherheitsfix.

## Offizielle Referenzen

- [AndroidX-Versionsübersicht](https://developer.android.com/jetpack/androidx/versions)
- [Navigation-Releases](https://developer.android.com/jetpack/androidx/releases/navigation)
- [Android-Backup-Sicherheit](https://developer.android.com/privacy-and-security/risks/backup-best-practices)
- [Material Components Releases](https://github.com/material-components/material-components-android/releases)
- [Gson-Releases](https://github.com/google/gson/releases)
- [Android Gradle Plugin](https://developer.android.com/build/releases/about-agp)
- [Gradle-Prüfsummen und Wrapper-Sicherheit](https://gradle.org/release-checksums/)
