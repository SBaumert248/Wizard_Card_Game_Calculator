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

Bei der Erstellung dieser Dokumentation konnten die Tests nicht ausgeführt
werden, weil in der verwendeten Umgebung weder `JAVA_HOME` gesetzt noch
`java` im `PATH` verfügbar war. Die Aussagen zu den Tests beruhen deshalb auf
einer statischen Prüfung des Codes.

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
die erste Runde wird `roundNumber - 1` als vorherige Runde abgefragt. Die
Methoden des Punktemodells sind nicht durchgängig gegen negative Rundenindizes
abgesichert. Dieser Pfad sollte vor einem Release reproduzierbar getestet und
robust gemacht werden.

### Fehlerbehandlung beim Laden

`WizardGame.loadFromJson()` behandelt nur `IOException`. Syntaxfehler oder
inkompatible JSON-Strukturen können als Gson-Laufzeitfehler auftreten. Für die
UI gibt es dabei keine verständliche Fehlermeldung oder einen kontrollierten
Fallback.

### Zustandsmodell

`WizardGame.startGame()` setzt `isGameRunning` auch bei einer ungültigen
Spielerzahl auf `true`, während `canStart()` dann `false` meldet. Die UI liefert
zwar nur gültige Werte, das Modell selbst besitzt aber einen widersprüchlichen
Zustand.

### Kopplung von UI und Domäne

Android-Resource-IDs dienen als Spieler-IDs. Dadurch ist `WizardGame` indirekt
von der konkreten View-Struktur abhängig. Eigene IDs oder ein `Player`-Modell
würden Tests, Migrationen und spätere UI-Änderungen vereinfachen.

### Verantwortlichkeiten

Validierungslogik für die Stichsumme liegt im Fragment, die übrige Spiellogik
im Controller und Modell. Eine Verlagerung in eine Android-unabhängige
Domänenschicht würde die Logik leichter testbar machen.

### Nicht verwendete Ansätze und Dependencies

- `AppState.java` wird im aktuellen Ablauf nicht verwendet und ist
  unvollständig.
- Room ist konfiguriert, aber nicht implementiert.
- Mehrere kommentierte Codeblöcke und TODOs erschweren die Wartung.

### Produktlücken

- Es gibt keine Siegerermittlung oder Abschlussansicht.
- Es gibt keine Historienansicht aller Runden.
- Die Oberfläche ist ausschließlich deutsch.
- Die JSON-Persistenz besitzt keine Formatversion oder Migration.
- Für die wichtigsten Nutzerabläufe fehlen automatisierte UI-Tests.

