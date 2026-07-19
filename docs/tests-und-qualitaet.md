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

Zusätzliche Regressionstests prüfen, dass geänderte oder geleerte Eingaben
bereits berechnete Punktestände invalidieren und ein Spieler danach nicht mehr
fälschlich als fertig gilt.

Weitere Randfalltests decken unvollständige und manipulierte JSON-Spielstände,
fehlende Dateien, nicht beschreibbare Ziele, ungültige Spieleroperationen,
Lücken zwischen Runden und die Stichsummenregel des Master-Modus ab.

## Abgesicherte Spielzyklus-Fehler

- Beim Beenden einer Partie werden aktuelle und vorherige Punktanzeigen
  vollständig zurückgesetzt.
- Der zurückgesetzte Zustand wird sofort gespeichert, sodass ein Prozessende
  nicht zur Wiederherstellung der abgebrochenen Partie führt.
- Der Master-Modus ist beim ersten Start standardmäßig aktiv.
- Ungültige oder geleerte Eingaben entfernen den vorherigen Modellwert und
  sperren den Rundenwechsel.
- Eingabebeobachter werden nicht bei jedem Rundenwechsel erneut registriert.
- Beim Wiederherstellen bleiben gespeicherte Namen und Rundendaten erhalten.

### Instrumentierte Tests

`WizardGameUiTest` führt die ersten kritischen Nutzerabläufe mit Espresso und
`ActivityScenario` auf einem echten Android-Gerät oder Emulator aus:

- vier Spieler und aktiver Master-Modus als Standardkonfiguration,
- Sperren des Rundenwechsels, sobald eine erforderliche Eingabe wieder
  geleert wird,
- vollständiges Entfernen alter Punkte beim Beenden und Neustarten einer
  Partie sowie
- Wiederherstellung von Runde, Spielername und letztem Punktestand nach einem
  Activity-Neustart,
- 20 Verlaufszeilen und drei Spielergruppen bei einer Partie mit drei
  Spielern sowie
- korrekte Verlaufssymbole, aktuelle Eingaben, kumulierte Punkte und leere
  Zukunftswerte sowie
- Rückkehr aus der Verlaufsansicht zur weiterhin vollständig befüllten
  Hauptspielansicht,
- erneute Verfügbarkeit des Drei-Punkte-Menüs nach der Rückkehr sowie
- Markierung ausschließlich der aktuellen Runde in der Verlaufstabelle und
- korrekte Anzeige von `0` Stichen und negativen Punkteständen für alle
  Spieler nach einem Rundenwechsel sowie
- Übernahme der abgeschlossenen Punktestände aller Spieler in die
  Vor-Runden-Anzeige der Hauptspielansicht.

Jeder Test löscht zuvor gezielt Einstellungen, Spielernamen, `game.json` und
den Singleton-Zustand. Dadurch sind die Tests voneinander und von zuvor
manuell gespielten Partien unabhängig.

## Ausführung

```powershell
.\gradlew.bat test
```

Für UI-Tests mit verbundenem Gerät oder Emulator:

```powershell
.\gradlew.bat connectedDebugAndroidTest
```

Für einen HTML- und XML-Coverage-Bericht der lokalen Debug-Tests:

```powershell
.\gradlew.bat jacocoTestReport
```

Für einen kombinierten Bericht aus lokalen Unit-Tests und instrumentierten
UI-Tests muss ein Android-Gerät oder Emulator verbunden sein:

```powershell
.\gradlew.bat jacocoCombinedTestReport
```

Der kombinierte HTML-Bericht liegt anschließend unter
`app/build/reports/jacoco/jacocoCombinedTestReport/html/index.html`, der
maschinenlesbare XML-Bericht im übergeordneten Verzeichnis.

Beide Berichte messen bewusst auch nicht ausgeführten Android-UI-Code. Es
werden keine Produktivklassen zur künstlichen Erhöhung der Quote
ausgeschlossen.

Aktueller Stand nach 42 Unit-Tests:

| Bereich | Zeilenabdeckung |
| --- | ---: |
| Model | 97,9 % |
| Controller | 94,4 % |
| View | 0,0 % |
| Gesamt | 28,8 % |

Kombinierter Stand nach 42 Unit-Tests und sechs UI-Tests:

| Bereich | Zeilenabdeckung |
| --- | ---: |
| Model | 100,0 % |
| Controller | 95,0 % |
| View | 92,0 % |
| Gesamt | 93,4 % |
| Branches gesamt | 77,0 % |

Der reine Unit-Test-Bericht bleibt bewusst verfügbar, weil er schnell und ohne
Android-Gerät ausgeführt werden kann. Der kombinierte Bericht ergänzt die
tatsächlich auf dem Gerät durchlaufenen Activity-, Fragment- und
Persistenzpfade.

Eine Mindestquote wird noch nicht erzwungen. Dafür sollte zunächst festgelegt
werden, ob in der lokalen Entwicklung der schnelle Unit-Wert oder in einer
Geräte-CI der kombinierte Wert verbindlich sein soll.

Nach Installation des Android SDK wurde der vollständige Gradle-Lauf
`test lintDebug assembleDebug jacocoCombinedTestReport` erfolgreich ausgeführt. Alle 42 lokalen
Unit-Tests bestanden sowohl für Debug als auch für Release. Android Lint
meldete keine Fehler; der Debug-Build erzeugte erfolgreich eine APK.

Zusätzlich bestanden alle sechs instrumentierten UI-Tests auf einem Pixel 9 Pro
mit Android 17.

Lint meldet weiterhin nicht blockierende Hinweise, vor allem zu ungenutzten
Ressourcen sowie zur Barrierefreiheit bestehender Eingabefelder und Bilder.
Diese sollten schrittweise abgearbeitet werden, ändern aber den erfolgreichen
Prüfstatus nicht.

## Empfohlene nächste Tests

Prioritär sinnvoll sind:

1. Wechsel zwischen App-Hintergrund und Vordergrund während einer Partie
2. Master-Modus: exakt ein fehlender Stich gegenüber anderen Abweichungen
3. Abschluss der letzten Runde und sichtbares Nutzerfeedback
4. ungültige, sehr große und während der Eingabe geänderte Zahlen
5. Konfigurationswechsel wie Drehen des Geräts
6. beschädigte oder inkompatible `game.json`

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
- Die UI-Testbasis deckt erst vier zentrale Abläufe ab und benötigt weitere
  Fehler-, Lebenszyklus- und Abschlussfälle.
