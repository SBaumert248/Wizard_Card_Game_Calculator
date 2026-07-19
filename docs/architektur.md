# Architektur

## Überblick

Die App besteht aus einem einzelnen Android-Modul und folgt einer einfachen,
nicht frameworkgebundenen MVC-ähnlichen Aufteilung:

```text
Android UI
├── MainActivity
│   ├── Navigation und Menü
│   ├── Start/Abbruch einer Partie
│   └── Speichern/Laden des Spielstands
├── FirstFragment
│   └── Spielernamen, Rundeneingaben und Anzeige
└── SecondFragment
    └── Einstellungen
          │
          ▼
WizardGame (Singleton-Controller)
├── Spieler, Runde und Spielstatus
├── Ablaufsteuerung und Validierung
└── JSON-Persistenz
          │
          ▼
Points (Modell je Spieler)
├── Ansagen je Runde
├── Ergebnisse je Runde
└── Punkteberechnung
```

Android View Binding ist aktiviert. Die Navigation zwischen den beiden
Fragmenten erfolgt über AndroidX Navigation und `nav_graph.xml`.

## Komponenten

### `MainActivity`

`MainActivity` ist der Einstiegspunkt und Host des Navigationsfragments. Sie

- richtet Toolbar, Menü und Navigation ein,
- erstellt den Pfad zur internen Datei `game.json`,
- startet eine Partie mit der in `SharedPreferences` gespeicherten Spielerzahl,
- koordiniert Aktualisierungen des Spielfragments,
- speichert den Spielzustand in `onPause()` und
- versucht ihn in `onCreate()` wiederherzustellen.

Die Activity aktiviert und deaktiviert die Menüaktionen abhängig davon, ob
bereits eine Partie läuft.

### `FirstFragment`

Das Hauptfragment enthält die eigentliche Spieloberfläche. Zu seinen Aufgaben
gehören:

- Laden und Speichern der zuletzt verwendeten Spielernamen,
- Sichtbarkeit der Spielerzeilen für drei bis sechs Spieler,
- Übernahme der Namen in `WizardGame`,
- Beobachtung der Eingabefelder über `TextWatcher`,
- Anzeige kumulierter Punkte,
- Prüfung leerer Felder und der Summe erzielter Stiche,
- Wechsel zur nächsten Runde und
- optionale Anzeige der Punktestände der vorherigen Runde.

Als Spieler-ID wird jeweils die Android-Resource-ID des Namensfeldes verwendet.
Das koppelt die Domänenlogik an die UI-Ressourcen und sollte bei einer späteren
Überarbeitung durch eigene stabile IDs ersetzt werden.

### `SecondFragment`

Das Einstellungsfragment verwaltet Spielerzahl, Master-Modus und die Anzeige
der letzten Punkte. Es liest die Einstellungen beim Erzeugen der View und
schreibt sie in `onPause()` in die Preferences-Datei `Setting`.

### `WizardGame`

`WizardGame` ist ein pro Prozess einmal vorhandenes Singleton. Es verwaltet:

- eine geordnete Liste von Spieler-IDs,
- Namen nach Spieler-ID,
- ein `Points`-Objekt je Spieler,
- aktuelle und maximale Rundenzahl und
- den Status, ob eine Partie läuft.

`startGame()` ordnet der Spielerzahl die Rundenzahl zu. `nextRound()` erhöht
die aktuelle Runde nur bis zur letzten zulässigen Runde. Eine Partie gilt als
beendet, wenn die letzte Runde erreicht ist und für alle Spieler ein berechneter
Punktestand dieser Runde vorliegt.

Das Singleton wird für das Laden nicht ersetzt. Stattdessen werden die Daten
des deserialisierten Objekts mit `copyFrom()` in die vorhandene Instanz kopiert.

### `Points`

Für jeden Spieler hält `Points` drei gleich lange `Integer`-Arrays:

- `predictions`
- `results`
- `scores`

Der Index entspricht der nullbasierten Runde. `getScore(round)` berechnet
zunächst die Rundenpunkte und summiert anschließend alle Punktwerte bis zur
angefragten Runde. Fehlende oder ungültige Werte werden durch `-1` gemeldet.

## Datenfluss einer Eingabe

1. Ein `TextWatcher` im `FirstFragment` erkennt eine gültige Zahl.
2. Die Resource-ID des Feldes wird einer Spieler-ID zugeordnet.
3. Ansage oder Ergebnis werden über `WizardGame` an das zugehörige
   `Points`-Objekt geschrieben.
4. `Points.getScore()` berechnet und summiert die Punkte.
5. Das Fragment aktualisiert das Punktfeld.
6. Wenn alle Spieler fertig sind, prüft das Fragment Felder und Stichsumme.
7. Bei gültigem Zustand wird der Button für die nächste Runde sichtbar.

## Persistenz

| Daten | Speicher | Zeitpunkt |
| --- | --- | --- |
| Laufende Partie | interne Datei `game.json`, Gson | Speichern in `MainActivity.onPause()`, Laden beim Start |
| Spielerzahl, Master-Modus, letzte Punkte | `SharedPreferences` namens `Setting` | Lesen beim Anzeigen, Schreiben beim Verlassen der Einstellungen |
| Spielernamen | `SharedPreferences` namens `Playernames` | Lesen beim Erzeugen, Schreiben in `FirstFragment.onPause()` |

Die deklarierte Room-Abhängigkeit wird derzeit nicht verwendet. Auch
`Model/AppState.java` ist ein unvollständiger, im aktuellen Ablauf nicht
verwendeter Ansatz für Preferences.

## Ressourcen und Darstellung

- `activity_main.xml` stellt Toolbar, NavHost und Start-FAB bereit.
- `fragment_first.xml` enthält die Spieltabelle.
- `fragment_second.xml` enthält die Einstellungen.
- Varianten von `dimens.xml` unterstützen Querformat und größere Displays.
- Theme-Varianten existieren für Tag, Nacht und API 23+.
- Texte sind nur in `values/strings.xml` vorhanden; eine echte
  Mehrsprachigkeit ist noch nicht eingerichtet.

