package de.sbaumert.wizardhelper.Controller;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;

import de.sbaumert.wizardhelper.Model.Points;

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

    public boolean saveToJson(String filePath) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setStrictness(Strictness.STRICT)
                .create();
        Path target = Paths.get(filePath);
        Path temporary = Paths.get(filePath + ".tmp");

        try (FileOutputStream output = new FileOutputStream(temporary.toFile());
             OutputStreamWriter writer = new OutputStreamWriter(output, StandardCharsets.UTF_8)) {
            gson.toJson(this, writer);
            writer.flush();
            output.getFD().sync();
        } catch (IOException e) {
            temporary.toFile().delete();
            return false;
        }

        try {
            try {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException e) {
            temporary.toFile().delete();
            return false;
        }
    }

    public boolean loadFromJson(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }

        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder()
                    .setStrictness(Strictness.STRICT)
                    .create();
            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.setStrictness(Strictness.STRICT);
            JsonElement json = JsonParser.parseReader(jsonReader);
            if (!hasRequiredFields(json)) {
                return false;
            }

            WizardGame loadedGame = gson.fromJson(json, WizardGame.class);
            if (!isValidLoadedGame(loadedGame)) {
                return false;
            }

            this.resetGame();
            this.copyFrom(loadedGame);
            return true;
        } catch (IOException | RuntimeException e) {
            return false;
        }
    }

    private static boolean isValidLoadedGame(WizardGame game) {
        if (game == null
                || game.playerIds == null
                || game.playerScores == null
                || game.playernames == null) {
            return false;
        }

        if (!game.isGameRunning) {
            return game.roundNumber == 0
                    && game.maxRoundNumber == -1
                    && game.playerIds.isEmpty()
                    && game.playerScores.isEmpty()
                    && game.playernames.isEmpty();
        }

        int expectedPlayers = playerCountForRounds(game.maxRoundNumber);
        if (expectedPlayers == 0
                || game.roundNumber < 0
                || game.roundNumber >= game.maxRoundNumber
                || game.playerIds.size() != expectedPlayers
                || new HashSet<>(game.playerIds).size() != expectedPlayers
                || game.playerScores.size() != expectedPlayers
                || game.playernames.size() != expectedPlayers) {
            return false;
        }

        for (Integer playerId : game.playerIds) {
            Points points = game.playerScores.get(playerId);
            String playerName = game.playernames.get(playerId);
            if (playerId == null
                    || playerName == null
                    || points == null
                    || !points.isValidForRounds(game.maxRoundNumber)) {
                return false;
            }
        }
        return true;
    }

    private void copyFrom(WizardGame other) {
        this.playernames.putAll(other.playernames);
        this.playerScores.putAll(other.playerScores);
        this.playerIds.addAll(other.playerIds);
        this.roundNumber = other.roundNumber;
        this.maxRoundNumber = other.maxRoundNumber;
        this.isGameRunning = other.isGameRunning;
    }

    public void startGame(int playerCount) {
        this.maxRoundNumber = switch (playerCount) {
            case 3 -> 20;
            case 4 -> 15;
            case 5 -> 12;
            case 6 -> 10;
            default -> -1;
        };
        this.isGameRunning = this.maxRoundNumber > 0;
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

    public boolean setPrediction(int playerId, int value, int round){
        if (playerId == -1)
            return false;
        Points score = this.playerScores.get(playerId);
        if (score != null) {
            return score.setPrediction(value, round);
        }
        return false;
    }

    public boolean setResult(int playerId,int value, int round) {
        if (playerId == -1)
            return false;
        Points score = this.playerScores.get(playerId);
        if (score != null) {
            return score.setResult(value, round);
        }
        return false;
    }

    public void clearPrediction(int playerId, int round) {
        Points score = this.playerScores.get(playerId);
        if (score != null) {
            score.clearPrediction(round);
        }
    }

    private static boolean hasRequiredFields(JsonElement json) {
        if (json == null || !json.isJsonObject()) {
            return false;
        }

        JsonObject object = json.getAsJsonObject();
        return object.has("playerIds")
                && object.has("playerScores")
                && object.has("playernames")
                && object.has("roundNumber")
                && object.has("maxRoundNumber")
                && object.has("isGameRunning");
    }

    public void clearResult(int playerId, int round) {
        Points score = this.playerScores.get(playerId);
        if (score != null) {
            score.clearResult(round);
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

    public int getPrediction(int playerId, int round) {
        Points points = this.playerScores.get(playerId);
        return points == null ? -1 : points.getPrediction(round);
    }

    public int getResult(int playerId, int round) {
        Points points = this.playerScores.get(playerId);
        return points == null ? -1 : points.getResult(round);
    }

    public List<Integer> getPlayerIds() {
        return List.copyOf(this.playerIds);
    }

    public String getPlayerName(int id){
        return this.playernames.get(id);
    }

    public int numOfPlayer() {
        return playerCountForRounds(this.maxRoundNumber);
    }

    private static int playerCountForRounds(int rounds) {
        return switch (rounds) {
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
        return playerScore != null && playerScore.countOfScore() == this.roundNumber+1;
    }

    public void nextRound(){
        if (this.roundNumber+1 < this.maxRoundNumber) {
            this.roundNumber++;
        }
    }
}
