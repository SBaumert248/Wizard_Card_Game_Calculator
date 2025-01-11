package com.example.Wizard_Helper_v2.Controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.example.Wizard_Helper_v2.Model.Player;

public class WizardGame {

    private static WizardGame instance;
    private final ArrayList<Player> players;
    private int roundNumber;
    private int maxRoundNumber;
    private boolean isGameRunning;

    private WizardGame() {
        this.roundNumber = 1;
        this.maxRoundNumber = -1;
        this.players = new ArrayList<>();
        this.isGameRunning = false;
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
    public static void loadFromJson(String filePath) {
        Gson gson = new Gson();
        File file = new File(filePath);
        if (!file.exists())
            return;
        try (FileReader reader = new FileReader(filePath)) {
            instance = gson.fromJson(reader, WizardGame.class);
            instance.removeTooMuchPlayer();
            System.out.println("Daten erfolgreich geladen aus " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void removeTooMuchPlayer(){
        int maxPlayer = switch (this.maxRoundNumber) {
            case 20 -> 3;
            case 15 -> 4;
            case 12 -> 5;
            case 10 -> 6;
            default -> -1;
        };
        if (maxPlayer == -1){
            return;
        }
        // Rückwärts durch die Liste iterieren, um sicher zu entfernen
        for (int idx = this.players.size() - 1; idx >= maxPlayer; idx--) {
            this.players.remove(idx);
        }
    }

    public void addPlayer(String name){
        Player player = new Player(name);
        this.players.add(player);
    }

    public void resetGame(){
        this.players.clear();
        this.roundNumber = 1;
        this.maxRoundNumber = -1;
        this.isGameRunning = false;
    }

    private Player getPlayer(String name){
        for (Player p: this.players) {
            if (Objects.equals(p.getName(), name)){
                return p;
            }
        }
        return null;
    }

    public void setPrediction(String playerName, int value){
        Player player = this.getPlayer(playerName);
        if (player != null) {
            player.addPrediction(value);
        }
    }

    public void correctPrediction(String playerName, int value){
        Player player = this.getPlayer(playerName);
        if (player != null) {
            player.removeLastPrecition();
            player.addPrediction(value);
        }
    }

    public void setResult(String playerName, int value) {
        Player player = this.getPlayer(playerName);
        if (player != null) {
            player.addResult(value);
        }
    }

    public void correctResult(String playerName, int value){
        Player player = this.getPlayer(playerName);
        if (player != null) {
            player.removeLastResult();
            player.addResult(value);
        }
    }

    public int getScore(String playerName){
        Player player = this.getPlayer(playerName);
        if (player != null) {
            return player.getScore();
        }
        return -1;
    }

    public int getScoreOfRound(String playerName, int round){
        Player player = this.getPlayer(playerName);
        if (round < 1 || round > this.maxRoundNumber || player == null){
            return -1;
        }
        return player.getScoreOfRound(round);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getRoundNumber(){
        return this.roundNumber;
    }

    public int getMaxRoundNumber(){
        return this.maxRoundNumber;
    }

    public int getActPrediction(String playerName){
        Player player = this.getPlayer(playerName);
        if (player == null){
            return -1;
        }
        return player.getPrediction();
    }

    public int getActResult(String playerName){
        Player player = this.getPlayer(playerName);
        if (player == null){
            return -1;
        }
        return player.getResult();
    }

    public boolean isRunning(){ return this.isGameRunning;}

    public boolean canStart(){
        return this.maxRoundNumber > -1;
    }

    public boolean isFinished(){
        return this.maxRoundNumber == this.roundNumber && this.allPlayerDone();
    }

    public boolean allPlayerDone(){
        boolean allDone = true;
        for (Player p: this.players) {
            allDone = allDone && p.isDone(this.roundNumber);
        }
        return allDone;
    }

    public boolean playerDone(String playerName){
        Player player = this.getPlayer(playerName);
        if (player != null){
            return player.isDone(this.roundNumber);
        }
        return false;
    }

    public void nextRound(){
        if (this.roundNumber < this.maxRoundNumber) {
            this.roundNumber++;
        }
    }
}
