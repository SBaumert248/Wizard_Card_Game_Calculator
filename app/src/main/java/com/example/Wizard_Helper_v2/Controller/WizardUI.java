package com.example.Wizard_Helper_v2.Controller;

import java.util.List;

import com.example.Wizard_Helper_v2.Model.Player;

public interface WizardUI {

    /**
     * Starts the Wizard game with the given number of players.
     * @param playerCount the number of players.
     */
    void startGame(int playerCount);

    /**
     * Adds a new player to the game.
     * @param playerName the name of the player.
     */
    void addPlayer(String playerName);

    /**
     * Resets the game to its initial state.
     */
    void resetGame();

    /**
     * Sets a prediction for a specific player in the current round.
     * @param playerName the name of the player.
     * @param value the predicted value.
     */
    void setPrediction(String playerName, int value);

    /**
     * Sets the result for a specific player in the current round.
     * @param playerName the name of the player.
     * @param value the result value.
     */
    void setResult(String playerName, int value);

    /**
     * Retrieves the score of a specific player.
     * @param playerName the name of the player.
     * @return the score of the player.
     */
    int getScore(String playerName);

    /**
     * Moves the game to the next round if applicable.
     */
    void nextRound();

    /**
     * Checks if the game is finished.
     * @return true if the game is finished, false otherwise.
     */
    boolean isGameFinished();

    /**
     * Retrieves the current round number.
     * @return the current round number.
     */
    int getRoundNumber();

    /**
     * Determines whether the game can be started. There must be at least 3 or at least 6 players.
     * @return whether the game can be started
     */
    boolean canStart();

    /**
     * Retrieves the max round number for actual number of players.
     * @return the max round number.
     */
    int getMaxRoundNumber();

    /**
     * Retrieves a list of all players in the game.
     * @return a list of players.
     */
    List<Player> getPlayers();
}