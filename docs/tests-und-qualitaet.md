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
  Activity-Neustart.

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

Der Bericht misst bewusst auch nicht ausgeführten Android-UI-Code. Dadurch
bleibt sichtbar, welche View-Pfade künftig instrumentierte Tests benötigen,
statt die Quote durch pauschales Ausschließen der UI künstlich zu erhöhen.

Aktueller Stand nach 41 Unit-Tests:

| Bereich | Zeilenabdeckung |
| --- | ---: |
| Model | 97,9 % |
| Controller | 94,3 % |
| View | 0,0 % |
| Gesamt | 31,6 % |

Die niedrige Gesamtquote entsteht vor allem durch die 552 nicht abgedeckten
Zeilen im Android-View-Paket. Für diesen Bereich sind instrumentierte Tests
oder eine weitere Verlagerung der Logik in Android-unabhängige Klassen nötig.
Eine Mindestquote wird noch nicht erzwungen, da ein globales Limit entweder
so niedrig wäre, dass es kaum schützt, oder den Build blockieren würde, bevor
eine UI-Testbasis vorhanden ist.

Nach Installation des Android SDK wurde der vollständige Gradle-Lauf
`test lintDebug assembleDebug jacocoTestReport` erfolgreich ausgeführt. Alle 41 lokalen
Unit-Tests bestanden sowohl für Debug als auch für Release. Android Lint
meldete keine Fehler; der Debug-Build erzeugte erfolgreich eine APK.

Zusätzlich bestanden alle vier instrumentierten UI-Tests auf einem Pixel 9 Pro
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
