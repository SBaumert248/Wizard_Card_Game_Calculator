package com.example.Wizard_Helper_v2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.Wizard_Helper_v2.Controller.WizardGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

class TestControllerWizardGame {

    private final WizardGame game = WizardGame.getInstance();

    @TempDir
    Path tempDir;

    @BeforeEach
    void ClearInstance(){
        game.resetGame();
    }

    @Test
    void init_game() {
        assertFalse(game.canStart());
        assertEquals(0, game.getRoundNumber(), "Actual round number should 0");
        assertEquals(0, game.numOfPlayer(), "Number of Players should be 0");
    }

    @Test
    void start_game_3_player(){
        game.startGame(3);
        assertTrue(game.canStart());
        assertEquals(20, game.getMaxRoundNumber(), "Number of Rounds should be 20 for 3 Player");
    }

    @Test
    void start_game_4_player(){
        game.startGame(4);
        assertTrue(game.canStart());
        assertEquals(15, game.getMaxRoundNumber(), "Number of Rounds should be 15 for 4 Player");
    }

    @Test
    void start_game_5_player(){
        game.startGame(5);
        assertTrue(game.canStart());
        assertEquals(12, game.getMaxRoundNumber(), "Number of Rounds should be 12 for 5 Player");
    }

    @Test
    void start_game_6_player(){
        game.startGame(6);
        assertTrue(game.canStart());
        assertEquals(10, game.getMaxRoundNumber(), "Number of Rounds should be 10 for 6 Player");
    }

    @Test
    void start_invalid_game(){
        game.startGame(7);
        assertFalse(game.canStart());
        assertFalse(game.isRunning());
        assertEquals(-1, game.getMaxRoundNumber(), "Number of Rounds should be -1 for invalid number of player (7)");
        game.startGame(2);
        assertFalse(game.canStart());
        assertFalse(game.isRunning());
        assertEquals(-1, game.getMaxRoundNumber(), "Number of Rounds should be -1 for invalid number of player (2)");
    }

    @Test
    void add_player(){
        game.startGame(3);

        game.addPlayer("Bob", 1);
        game.addPlayer("Alice", 2);
        game.addPlayer("Charlie", 3);
        assertEquals(3, game.numOfPlayer(), "Number of Player should be 3");
    }

    @Test
    void get_score_for_valid_player(){
        game.startGame(4);

        game.addPlayer("Bob", 1);
        game.setPrediction(1, 1, 0);
        game.setResult(1, 1, 0);
        assertEquals(30, game.getScore(1, 0), "Score of 'Bob' should be 30");
        game.nextRound();
        game.setPrediction(1, 2,1);
        game.setResult(1, 2,1);
        assertEquals(70, game.getScore(1, 1), "Score of 'Bob' should be 70");
    }

    @Test
    void get_score_for_invalid_player(){
        game.startGame(4);

        game.addPlayer("Bob", 1);
        game.setPrediction(1,1, 0);
        game.setResult(1, 1, 0);
        assertEquals(-1, game.getScore(999, 0), "Score of PlayerId 999 should be -1");
    }

    @Test
    void get_score_calculation_check(){
        game.startGame(4);

        game.addPlayer("Bob", 1);
        game.setPrediction(1, 1, 0);
        game.setResult(1, 0, 0);
        assertEquals(-10, game.getScore(1, 0), "Score of 'Bob' should be -10");

        game.setPrediction(1, 1, 1);
        game.setResult(1,1, 1);
        assertEquals(20, game.getScore(1, 1), "Score of 'Bob' should be 20");
    }

    String save_json(){
        game.startGame(3);

        game.addPlayer("Bob", 1);
        game.addPlayer("Alice", 2);
        game.addPlayer("Charlie", 3);

        game.setPrediction(1, 1, 0);
        game.setPrediction(2, 0, 0);
        game.setPrediction(3, 1, 0);

        game.setResult(1, 1, 0);
        game.setResult(2, 0, 0);
        game.setResult(3, 0, 0);

        game.getScore(1, 0);
        game.getScore(2, 0);
        game.getScore(3, 0);

        Path saveFile = tempDir.resolve("game.json");
        assertTrue(game.saveToJson(saveFile.toString()));
        return saveFile.toString();
    }

    @Test
    void load_json(){
        String saveFile = this.save_json();
        game.resetGame();
        assertTrue(game.loadFromJson(saveFile));

        assertEquals(3, game.numOfPlayer());
        assertTrue(game.isRunning());

        assertEquals(0, game.getRoundNumber());
        assertEquals(20, game.getMaxRoundNumber());

        assertEquals("Bob", game.getPlayerName(1));
        assertEquals(30, game.getScore(1, 0));
        assertEquals("Alice", game.getPlayerName(2));
        assertEquals(20, game.getScore(2, 0));
        assertEquals("Charlie", game.getPlayerName(3));
        assertEquals(-10, game.getScore(3, 0));
    }

    @Test
    void reject_corrupt_json() throws IOException {
        Path saveFile = tempDir.resolve("corrupt.json");
        Files.write(saveFile, "{not valid json".getBytes(StandardCharsets.UTF_8));

        assertFalse(game.loadFromJson(saveFile.toString()));
        assertFalse(game.isRunning());
    }

    @Test
    void clear_round_input_removes_completed_score() {
        game.startGame(3);
        game.addPlayer("Bob", 1);

        assertTrue(game.setPrediction(1, 1, 0));
        assertTrue(game.setResult(1, 1, 0));
        assertEquals(30, game.getScore(1, 0));
        assertTrue(game.playerDone(1));

        game.clearResult(1, 0);

        assertEquals(-1, game.getActResult(1));
        assertEquals(-1, game.getActScore(1));
        assertFalse(game.playerDone(1));
    }

    @Test
    void inactive_game_can_be_persisted_and_restored() {
        Path saveFile = tempDir.resolve("inactive.json");

        assertTrue(game.saveToJson(saveFile.toString()));
        game.startGame(3);
        assertTrue(game.isRunning());

        assertTrue(game.loadFromJson(saveFile.toString()));
        assertFalse(game.isRunning());
        assertEquals(0, game.numOfPlayer());
    }

    @Test
    void missing_or_invalid_save_targets_are_handled() {
        assertFalse(game.loadFromJson(tempDir.resolve("missing.json").toString()));
        assertFalse(game.saveToJson(tempDir.toString()));
    }

    @Test
    void invalid_player_operations_are_safe() {
        game.startGame(3);
        game.addPlayer("Bob", 1);

        assertFalse(game.setPrediction(-1, 0, 0));
        assertFalse(game.setPrediction(999, 0, 0));
        assertFalse(game.setResult(-1, 0, 0));
        assertFalse(game.setResult(999, 0, 0));
        assertEquals(-1, game.getActPrediction(999));
        assertEquals(-1, game.getActResult(999));
        assertEquals(-1, game.getActScore(999));
        assertFalse(game.playerDone(999));

        assertTrue(game.setPrediction(1, 1, 0));
        assertEquals(1, game.getActPrediction(1));
        game.clearPrediction(1, 0);
        assertEquals(-1, game.getActPrediction(1));
    }

    @Test
    void structurally_invalid_game_json_is_rejected() throws IOException {
        String[] invalidGames = {
                "null",
                "{}",
                "{\"playerIds\":[],\"playerScores\":{},\"playernames\":{},"
                        + "\"roundNumber\":0,\"maxRoundNumber\":20,\"isGameRunning\":true}",
                "{\"playerIds\":[1,1,1],\"playerScores\":{},\"playernames\":{},"
                        + "\"roundNumber\":0,\"maxRoundNumber\":20,\"isGameRunning\":true}"
        };

        for (int index = 0; index < invalidGames.length; index++) {
            Path saveFile = tempDir.resolve("invalid-" + index + ".json");
            Files.write(saveFile, invalidGames[index].getBytes(StandardCharsets.UTF_8));
            assertFalse(game.loadFromJson(saveFile.toString()), "Invalid JSON index " + index);
        }
    }

    @Test
    void test_all_player_done(){
        game.startGame(3);

        game.addPlayer("Bob", 1);
        game.addPlayer("Alice", 2);
        game.addPlayer("Charlie", 3);

        game.setPrediction(1, 1, 0);
        game.setPrediction(2, 0, 0);
        game.setPrediction(3, 1, 0);

        game.setResult(1, 1, 0);
        game.setResult(2, 0, 0);
        game.setResult(3, 0, 0);

        game.getScore(1, 0);
        game.getScore(2, 0);
        game.getScore(3, 0);

        assertTrue(game.allPlayerDone());
    }

    @Test
    void test_not_all_player_done(){
        game.startGame(3);

        game.addPlayer("Bob", 1);
        game.addPlayer("Alice", 2);
        game.addPlayer("Charlie", 3);

        game.setPrediction(1, 1, 0);
        game.setPrediction(2, 0, 0);
        game.setPrediction(3, 1, 0);

        game.setResult(1, 1, 0);
        game.setResult(3, 0, 0);

        game.getScore(1, 0);
        game.getScore(3, 0);

        assertFalse(game.allPlayerDone());
    }

    @Test
    void test_reset_game(){
        game.startGame(3);

        game.addPlayer("Bob", 1);
        game.addPlayer("Alice", 2);
        game.addPlayer("Charlie", 3);

        game.setPrediction(1, 1, 0);
        game.setPrediction(2, 0, 0);
        game.setPrediction(3, 1, 0);

        game.setResult(1, 1, 0);
        game.setResult(3, 0, 0);

        game.getScore(1, 0);
        game.getScore(3, 0);

        assertFalse(game.allPlayerDone());
    }

    @Test
    void test_isGameFinished(){
        game.startGame(3);

        game.addPlayer("Bob", 1);
        game.addPlayer("Alice", 2);
        game.addPlayer("Charlie", 3);

        assertFalse(game.isFinished());

        for (int round = 0; round < game.getMaxRoundNumber(); round++){
            game.setPrediction(1, round+1, round);
            game.setPrediction(2, 0, round);
            game.setPrediction(3, 1, round);

            game.setResult(1, round+1, round);
            game.setResult(2, 0, round);
            game.setResult(3, 0, round);

            game.getScore(1, round);
            game.getScore(2, round);
            game.getScore(3, round);

            game.nextRound();
        }

        assertTrue(game.isFinished());
    }
}
