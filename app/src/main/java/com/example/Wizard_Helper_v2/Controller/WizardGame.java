package com.example.Wizard_Helper_v2.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.example.Wizard_Helper_v2.Model.Points;

public class WizardGame {

    private static WizardGame instance;
    private final ArrayList<Integer> playerIds;
    private final Map<Integer, Points> playerScores;
    private final Map<Integer, String> playernames;
    private int roundNumber;
    private int maxRoundNumber;
    private boolean isGameRunning;


    private WizardGame() {
        this.playerIds = new ArrayList<>();
        this.playerScores = new HashMap<>();
        this.playernames = new HashMap<>();
        this.resetGame();
    }

    // Öffentliche statische Methode, um die Instanz zu erhalten
    public static synchronized WizardGame getInstance() {
        if (instance == null) {
            instance = new WizardGame();
        }
        return instance;
    }

    // Funktion zum Speichern in eine JSON-Datei
    public void saveToJson(String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(this, writer);
            System.out.println("Daten erfolgreich gespeichert in " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Funktion zum Laden aus einer JSON-Datei
    public void loadFromJson(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Datei nicht gefunden: " + filePath);
            return;
        }

        try (FileReader r = new FileReader(filePath)) {
            Gson gson = new Gson();

            // 1) JSON --> temporäres Objekt
            WizardGame loadedGame = gson.fromJson(r, WizardGame.class);
            if (loadedGame == null) {
                throw new IOException("JSON leer oder nicht kompatibel: " + filePath);
            }

            // 2) Aktuelle Instanz zurücksetzen und befüllen
            this.resetGame();
            this.copyFrom(loadedGame);

            System.out.println("Daten erfolgreich geladen aus " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Übernimmt persistente Felder von `other` in **diese** Instanz. */
    private void copyFrom(WizardGame other) {
        this.playernames.putAll(other.playernames);
        this.playerScores.putAll(other.playerScores);
        this.playerIds.addAll(other.playerIds);
        this.roundNumber = other.roundNumber;
        this.maxRoundNumber = other.maxRoundNumber;
        this.isGameRunning = other.isGameRunning;
        /* … weitere Felder … */
    }

    public void startGame(int playerCount) {
        this.maxRoundNumber = switch (playerCount) {
            case 3 -> 20;
            case 4 -> 15;
            case 5 -> 12;
            case 6 -> 10;
            default -> -1;
        };
        this.isGameRunning = true;
    }

    public void addPlayer(String name, int id) {
        this.playernames.remove(id);
        this.playernames.put(id, name);

        if (!this.playerIds.contains(id))
            this.playerIds.add(id);

        if (!this.playerScores.containsKey(id))
            this.playerScores.put(id, new Points(this.maxRoundNumber));
    }

    public void resetGame(){
        this.playerIds.clear();
        this.playerScores.clear();
        this.playernames.clear();
        this.roundNumber = 0;
        this.maxRoundNumber = -1;
        this.isGameRunning = false;
    }

    public void setPrediction(int playerId, int value, int round){
        if (playerId == -1)
            return;
        Points score = this.playerScores.get(playerId);
        if (score != null) {
            score.setPrediction(value, round);
        }
    }

    public void setResult(int playerId,int value, int round) {
        if (playerId == -1)
            return;
        Points score = this.playerScores.get(playerId);
        if (score != null) {
            score.setResult(value, round);
        }
    }

    public int getScore(int playerId, int round){
        if (playerId > -1) {
            Points score = this.playerScores.get(playerId);
            if (score != null) {
                return score.getScore(round);
            }
        }
        return -1;
    }

    public String getPlayerName(int id){
        return this.playernames.get(id);
    }

    public int numOfPlayer() {
        return switch (this.maxRoundNumber) {
            case 20 -> 3;
            case 15 -> 4;
            case 12 -> 5;
            case 10 -> 6;
            default -> 0;
        };
    }

    /**
     * Liefert die aktuelle Rundennummer.
     *
     * <p><strong>Wichtig:</strong> Die Zählung beginnt bei {@code 0} statt {@code 1},
     * da die zugrunde liegenden Arrays nullbasiert sind.</p>
     *
     * @return die aktuelle (nullbasierte) Rundennummer
     */
    public int getRoundNumber(){
        return this.roundNumber;
    }

    public int getMaxRoundNumber(){
        return this.maxRoundNumber;
    }

    public int getActScore(int playerId){
        if (playerId > -1) {
            Points score = this.playerScores.get(playerId);
            if (score != null) {
                return score.getScore(this.roundNumber);
            }
        }
        return -1;
    }

    public int getActPrediction(int playerId){
        if (playerId > -1) {
            Points score = this.playerScores.get(playerId);
            if (score != null) {
                return score.getPrediction(this.roundNumber);
            }
        }
        return -1;
    }

    public int getActResult(int playerId){
        if (playerId > -1) {
            Points score = this.playerScores.get(playerId);
            if (score != null) {
                return score.getResult(this.roundNumber);
            }
        }
        return -1;

    }

    public boolean isRunning(){ return this.isGameRunning;}

    public boolean canStart(){
        return this.maxRoundNumber > -1;
    }

    /**
     * Gibt an, ob das gesamte Spiel beendet ist.
     *
     * <p>Ein Spiel gilt als abgeschlossen, wenn</p>
     * <ul>
     *   <li>die aktuelle Runde die letzte vorgesehene Runde ist
     *       ({@code roundNumber + 1 == maxRoundNumber}) <em>und</em></li>
     *   <li>alle Spieler ihre Einträge für diese Runde abgeschlossen haben
     *       ({@code allPlayerDone() == true}).</li>
     * </ul>
     *
     * <p>Auch hier ist {@code roundNumber} nullbasiert; daher wird für
     * Vergleiche stets {@code roundNumber + 1} verwendet.</p>
     *
     * @return {@code true}, wenn keine weiteren Runden mehr gespielt werden
     *         müssen und alle Spieler fertig sind; sonst {@code false}
     */
    public boolean isFinished(){
        return this.maxRoundNumber == this.roundNumber+1 && this.allPlayerDone();
    }

    public boolean allPlayerDone(){
        boolean allDone = true;
        for (int id: this.playerIds){
            allDone = allDone && this.playerDone(id);
        }
        return allDone;
    }

    /**
     * Prüft, ob der angegebene Spieler bereits alle Ergebnisse / Stiche / Gebote
     * bis einschließlich der aktuellen Runde eingetragen hat.
     *
     * <p><strong>Nullbasierte Rundenzählung:</strong>
     * {@code roundNumber} startet bei {@code 0}.
     * Darum muss ein Spieler nach Runde <code>n</code> genau
     * <code>n + 1</code> Einträge besitzen, um als »fertig« zu gelten.</p>
     *
     * @param playerId die eindeutige ID des Spielers
     * @return {@code true}, wenn der Spieler für jede bisher gespielte Runde
     *         einen Eintrag hat; andernfalls {@code false}
     */
    public boolean playerDone(int playerId){
        Points playerScore = this.playerScores.get(playerId);
        return playerScore.countOfScore() == this.roundNumber+1;
    }

    public void nextRound(){
        if (this.roundNumber+1 < this.maxRoundNumber) {
            this.roundNumber++;
        }
    }
}
