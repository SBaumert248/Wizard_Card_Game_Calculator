# Produkt und Bedienung

## Zweck

Wizard Helper nimmt einer Spielrunde die manuelle Punkteberechnung und
Fortschreibung ab. Für jeden Spieler werden pro Runde

1. die vorhergesagten Stiche,
2. die tatsächlich erzielten Stiche und
3. der kumulierte Punktestand

geführt.

Die App ist für drei bis sechs Spieler ausgelegt. Aus der Spielerzahl ergibt
sich die Anzahl der Runden:

| Spieler | Runden |
| ---: | ---: |
| 3 | 20 |
| 4 | 15 |
| 5 | 12 |
| 6 | 10 |

Intern sind Runden nullbasiert gespeichert. In der Oberfläche werden sie
benutzerfreundlich ab 1 angezeigt.

## Punkteberechnung

Die Berechnung folgt der im Code hinterlegten Wizard-Regel:

- Ansage getroffen: `20 + 10 × angesagte Stiche`
- Ansage verfehlt: `-10 × |Ansage - Ergebnis|`

Der angezeigte Wert ist der kumulierte Punktestand bis einschließlich der
aktuellen Runde.

Beispiele:

| Ansage | Ergebnis | Rundenpunkte |
| ---: | ---: | ---: |
| 0 | 0 | 20 |
| 2 | 2 | 40 |
| 3 | 1 | -20 |
| 1 | 4 | -30 |

Ein noch nicht vollständig befüllter Datensatz wird intern durch `-1`
signalisiert und in der Oberfläche als `?` dargestellt.

## Typischer Ablauf

### 1. Partie vorbereiten

Auf dem Hauptbildschirm werden die Spielernamen eingetragen. Die zuletzt
verwendeten Namen werden lokal gespeichert und beim nächsten Start wieder
eingesetzt.

Über das Menü **Einstellung** lassen sich Spielerzahl und Spieloptionen
festlegen. Die sichtbaren Spielerzeilen passen sich an die gewählte Anzahl an.

### 2. Partie starten

Der gelbe schwebende Start-Button startet eine neue Partie mit den aktuellen
Einstellungen. Danach

- werden die Namen in den Spielzustand übernommen,
- werden die Eingabefelder für Ansage und Ergebnis aktiviert,
- wird die Rundennummer angezeigt,
- wird der Start-Button ausgeblendet und
- werden Änderungen an den Einstellungen für die laufende Partie gesperrt.

### 3. Runde erfassen

Für jeden sichtbaren Spieler werden Ansage und Ergebnis als nichtnegative ganze
Zahlen eingetragen. Sobald beide Werte vorliegen, berechnet die App den
kumulierten Punktestand.

Die nächste Runde wird erst angeboten, wenn

- alle sichtbaren Eingabefelder gültige Zahlen enthalten,
- für jeden Spieler ein Punktestand berechnet werden konnte und
- die Summe der tatsächlich erzielten Stiche zur Runde passt.

### 4. Nächste Runde

Mit **Nächste Runde** wird der Rundenzähler erhöht. Abhängig von der Einstellung
werden die Punktestände der vorherigen Runde zusätzlich eingeblendet.

In der letzten Runde wird kein weiterer Runden-Button mehr angezeigt. Eine
eigene Siegerauswertung oder Abschlussmeldung ist aktuell nicht implementiert.

### 5. Partie abbrechen

Über **Beende aktuelles Spiel** wird der Spielzustand zurückgesetzt und der
Start-Button wieder eingeblendet. Die zuletzt verwendeten Spielernamen und
allgemeinen Einstellungen bleiben erhalten.

## Einstellungen

Die Einstellungsseite enthält:

| Einstellung | Bedeutung | Standard im Code |
| --- | --- | --- |
| Spielerzahl | Legt sichtbare Spieler und Rundenzahl fest | 4 |
| Master Modus | Erlaubt, dass die Ergebnis-Summe genau einen Stich unter der Rundenzahl liegt | aus |
| Punkte der letzten Runde anzeigen | Blendet den vorherigen Punktestand ein | an |

Im normalen Modus muss die Summe der Ergebnisse der aktuellen Rundenzahl
entsprechen. Im Master-Modus wird zusätzlich eine um genau eins kleinere Summe
akzeptiert. Bei einer unzulässigen Summe zeigt die App einen Warnhinweis und
verhindert den Rundenwechsel.

## Lokales Verhalten

Ein laufendes Spiel wird beim Pausieren der Activity in der internen Datei
`game.json` gespeichert und beim App-Start wieder geladen. Einstellungen und
Spielernamen werden separat gespeichert. Die App benötigt laut Manifest keine
Netzwerkberechtigung.

Das Wiederherstellen eines laufenden Spiels besitzt aktuell bekannte
Randfälle; Details stehen in [Tests und Qualität](tests-und-qualitaet.md).

