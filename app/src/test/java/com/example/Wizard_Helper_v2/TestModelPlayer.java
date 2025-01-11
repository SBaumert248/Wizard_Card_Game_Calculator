package com.example.Wizard_Helper_v2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.Wizard_Helper_v2.Model.Player;

class TestModelPlayer {

    private final Player p = new Player("Bob");

    @Test
    void init_player() {
        assertEquals("Bob", p.getName(), "Playername should be 'Bob'");
        assertEquals(-1, p.getScore(), "Player score should be -1");
    }

    @Test
    void is_player_done() {
        assertFalse(p.isDone(1), "Player state should be not done");
        p.addPrediction(1);
        assertFalse(p.isDone(1), "Player state should be already not done");
        p.addResult(1);
        assertTrue(p.isDone(1), "Player state should be done");
    }
}
