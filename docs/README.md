# Projektdokumentation

Diese Dokumentation beschreibt den aktuellen Stand von **Wizard Helper**, einer
Android-App zur Erfassung und Berechnung der Punkte beim Kartenspiel Wizard.
Sie wurde aus dem Quellcode und der Build-Konfiguration abgeleitet.

## Dokumente

- [Produkt und Bedienung](produkt-und-bedienung.md) – Zweck, Spielablauf,
  Einstellungen und Punkteberechnung
- [Architektur](architektur.md) – Komponenten, Zuständigkeiten, Datenfluss und
  Persistenz
- [Entwicklung](entwicklung.md) – Voraussetzungen, Build, Projektstruktur und
  Konventionen
- [Tests und Qualität](tests-und-qualitaet.md) – vorhandene Tests,
  Testausführung und bekannte technische Risiken
- [Sicherheits- und Risikobericht](sicherheits-und-risikobericht.md) –
  behobene und verbleibende Risiken sowie Prüfnachweise

## Kurzprofil

| Eigenschaft | Stand |
| --- | --- |
| Plattform | Android |
| App-Modul | `app` |
| Implementierung | überwiegend Java, Gradle-Konfiguration in Kotlin DSL |
| Unterstützte Android-Version | ab Android 8.0 (API 26) |
| Ziel-SDK | API 35 |
| Sprache der Oberfläche | Deutsch |
| Spielerzahl | 3 bis 6 |
| Architektur | einfache Aufteilung in View, Controller und Model |
| Lokale Speicherung | JSON-Datei und `SharedPreferences` |

Die App ersetzt keinen Regelsatz. Sie unterstützt eine laufende Wizard-Partie,
indem sie Ansagen und tatsächlich erzielte Stiche erfasst, die Punktstände
berechnet und die zulässige Stichsumme einer Runde prüft.
