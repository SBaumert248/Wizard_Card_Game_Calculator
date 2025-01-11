package com.example.Wizard_Helper_v2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.Wizard_Helper_v2.Controller.WizardGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestControllerWizardGame {


    private final WizardGame game = WizardGame.getInstance();

    @BeforeEach
    void ClearInstance(){
        game.resetGame();
    }

    @Test
    void init_game() {
        assertFalse(game.canStart());
        assertEquals(1, game.getRoundNumber(), "Actual round number should 1");
        assertEquals(0, game.getPlayers().size(), "Number of Players should be 0");
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
        assertEquals(-1, game.getMaxRoundNumber(), "Number of Rounds should be -1 for invalid number of player (7)");
        game.startGame(2);
        assertFalse(game.canStart());
        assertEquals(-1, game.getMaxRoundNumber(), "Number of Rounds should be -1 for invalid number of player (2)");
    }

    @Test
    void add_player(){
        game.addPlayer("Bob");
        game.addPlayer("Alice");
        game.addPlayer("Charlie");
        assertEquals(3, game.getPlayers().size(), "Number of Player should be 3");
    }

    @Test
    void get_score_for_valid_player(){
        game.addPlayer("Bob");
        game.setPrediction("Bob", 1);
        game.setResult("Bob", 1);
        assertEquals(30, game.getScore("Bob"), "Score of 'Bob' should be 30");
        game.nextRound();
        game.setPrediction("Bob", 2);
        game.setResult("Bob", 2);
        assertEquals(70, game.getScore("Bob"), "Score of 'Bob' should be 70");
    }

    @Test
    void get_score_for_invalid_player(){
        game.addPlayer("Bob");
        game.setPrediction("Bob", 1);
        game.setResult("Bob", 1);
        assertEquals(-1, game.getScore("Hans"), "Score of 'Hans' should be -1");
    }

    @Test
    void get_score_calculation_check(){
        game.addPlayer("Bob");
        game.setPrediction("Bob", 10);
        game.setResult("Bob", 1);
        assertEquals(-90, game.getScore("Bob"), "Score of 'Bob' should be -90");
        game.addPlayer("Bob");
        game.setPrediction("Bob", 1);
        game.setResult("Bob", 1);
        assertEquals(-60, game.getScore("Bob"), "Score of 'Bob' should be -60");
    }

    void save_json(){
        game.addPlayer("Bob");
        game.addPlayer("Alice");
        game.addPlayer("Charlie");
        game.startGame(3);
        game.setPrediction("Bob", 1);
        game.setPrediction("Alice", 0);
        game.setPrediction("Charlie", 1);
        game.setResult("Bob", 1);
        game.setResult("Alice", 0);
        game.setResult("Charlie", 0);
        game.getScore("Bob");
        game.getScore("Alice");
        game.getScore("Charlie");

        game.saveToJson(".\\testfile.json");

    }

    @Test
    void load_json(){
        this.save_json();
        assertEquals(3, game.getPlayers().size());
        assertTrue(game.isRunning());
        assertEquals(1, game.getRoundNumber());
        assertEquals(20, game.getMaxRoundNumber());
        assertEquals("Bob", game.getPlayers().get(0).getName());
        assertEquals(30, game.getPlayers().get(0).getScore());
        assertEquals("Alice", game.getPlayers().get(1).getName());
        assertEquals(20, game.getPlayers().get(1).getScore());
        assertEquals("Charlie", game.getPlayers().get(2).getName());
        assertEquals(-10, game.getPlayers().get(2).getScore());
    }

    //TODO: test for isGameFinished
    //TODO: test for allPlayerDone
    //TODO: test for nextRound
    //TODO: test for resetGame

}