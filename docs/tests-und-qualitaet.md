# Tests und Qualität

## Vorhandene Tests

### Lokale Unit-Tests

`TestModelPoints` deckt unter anderem ab:

- korrekte und verfehlte Ansagen,
- kumulierte Punktestände,
- nachträgliches Ändern einer Runde,
- fehlende Werte,
- negative Eingaben und
- Zähler für Ansagen und Ergebnisse.

`TestControllerWizardGame` prüft unter anderem:

- Initialzustand und Reset,
- Rundenzahlen für drei bis sechs Spieler,
- ungültige Spielerzahlen,
- Spieler und Punktabfragen,
- Rundenfortschritt und Spielende,
- Vollständigkeit einer Runde sowie
- JSON-Speichern und -Laden.

### Instrumentierte Tests

Es existiert ein `ExampleInstrumentedTest`. Eine systematische UI-Abdeckung mit
Espresso ist aktuell nicht vorhanden.

## Ausführung

```powershell
.\gradlew.bat test
```

Für UI-Tests mit verbundenem Gerät oder Emulator:

```powershell
.\gradlew.bat connectedAndroidTest
```

Bei der Risikoüberarbeitung wurden der Android-unabhängige Java-Kern und seine
Tests erfolgreich mit JDK 17 kompiliert. Alle 28 lokalen Unit-Tests liefen mit
JUnit Jupiter 5.13.4 und Gson 2.14.0 erfolgreich. Der vollständige Gradle-Test-
und Lint-Lauf konnte nicht gestartet werden, weil auf dem verwendeten Rechner
kein Android SDK konfiguriert oder auffindbar war. Gradle 8.13 selbst wurde
erfolgreich gestartet.

## Empfohlene nächste Tests

Prioritär sinnvoll sind:

1. Wiederherstellung in Runde 1 und in späteren Runden
2. Wechsel zwischen App-Hintergrund und Vordergrund während einer Partie
3. Master-Modus: exakt ein fehlender Stich gegenüber anderen Abweichungen
4. Abschluss der letzten Runde und sichtbares Nutzerfeedback
5. ungültige, sehr große und während der Eingabe geänderte Zahlen
6. Konfigurationswechsel wie Drehen des Geräts
7. beschädigte oder inkompatible `game.json`

## Bekannte Risiken und technische Schulden

### Wiederherstellung des Spielstands

`FirstFragment.loadLastGame()` ist im Code mit TODOs versehen. Besonders für
die erste Runde wird `roundNumber - 1` als vorherige Runde abgefragt. Das
Punktemodell weist negative Indizes inzwischen sicher ab; das fachlich korrekte
Darstellungsverhalten nach Prozesswiederherstellung sollte dennoch mit einem
instrumentierten UI-Test abgesichert werden.

### Fehlerbehandlung beim Laden

`WizardGame.loadFromJson()` fängt Ein-/Ausgabe- und Parserfehler ab und
übernimmt Daten erst nach einer Struktur- und Werteprüfung. Für die UI gibt es
bei einem abgewiesenen Spielstand jedoch noch keine verständliche Meldung.

### Zustandsmodell

Ungültige Spielerzahlen starten keine Partie mehr. Ein Regressionstest deckt
diesen Zustand ab.

### Kopplung von UI und Domäne

Android-Resource-IDs dienen als Spieler-IDs. Dadurch ist `WizardGame` indirekt
von der konkreten View-Struktur abhängig. Eigene IDs oder ein `Player`-Modell
würden Tests, Migrationen und spätere UI-Änderungen vereinfachen.

### Verantwortlichkeiten

Validierungslogik für die Stichsumme liegt im Fragment, die übrige Spiellogik
im Controller und Modell. Eine Verlagerung in eine Android-unabhängige
Domänenschicht würde die Logik leichter testbar machen.

### Nicht verwendeter Code

Mehrere kommentierte Codeblöcke und TODOs erschweren weiterhin die Wartung.

### Produktlücken

- Es gibt keine Siegerermittlung oder Abschlussansicht.
- Es gibt keine Historienansicht aller Runden.
- Die Oberfläche ist ausschließlich deutsch.
- Die JSON-Persistenz besitzt keine Formatversion oder Migration.
- Für die wichtigsten Nutzerabläufe fehlen automatisierte UI-Tests.
