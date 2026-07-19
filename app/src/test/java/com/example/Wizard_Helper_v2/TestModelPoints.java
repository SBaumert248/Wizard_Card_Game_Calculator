package com.example.Wizard_Helper_v2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.Wizard_Helper_v2.Model.Points;

class TestModelPoints {

    private final Points p = new Points(2);

    @Test
    void addition_isCorrect() {
        int round = 0;
        p.setPrediction(1, round);
        p.setResult(1, round);
        assertEquals(30, p.getScore(round), "Score should return 30");
        round =+ 1;
        p.setPrediction(1, round);
        p.setResult(1, round);
        assertEquals(60, p.getScore(round), "Score should return 60");
    }

    @Test
    void addition_isCorrect_wrongOrder() {
        p.setResult(1, 0);
        p.setPrediction(1, 0);
        assertEquals(30, p.getScore(0), "Score should return 30");
    }

    @Test
    void addition_isCorrect_resetScoreForRound() {
        p.setPrediction(1, 0);
        p.setResult(0, 0);
        assertEquals(-10, p.getScore(0), "Score should return -10");
        p.setPrediction(1, 0);
        p.setResult(1, 0);
        assertEquals(30, p.getScore(0), "Score should return 30");
    }

    @Test
    void addition_isCorrect_noResult() {
        p.setPrediction(0, 0);
        p.setResult(0, 0);
        assertEquals(20, p.getScore(0), "Score should return 20");
    }

    @Test
    void score_after_prediction(){
        p.setPrediction(0, 0);
        assertEquals(-1, p.getScore(0), "Score should return -1");
    }

    @Test
    void score_before_prediction(){
        assertEquals(-1, p.getScore(0), "Score should return -1");
    }

    @Test
    void get_number_of_prediction(){
        assertEquals(0, p.countOfPredictions(), "Count of Prediction should be 0");
        p.setPrediction(1, 0);
        assertEquals(1, p.countOfPredictions(), "Count of Prediction should be 1");
    }

    @Test
    void add_invalid_prediction(){
        p.setPrediction(-1, 0);
        assertEquals(0, p.countOfPredictions(), "Count of Prediction should be 0");
        p.setPrediction(2, 0);
        assertEquals(0, p.countOfPredictions(), "Prediction cannot exceed tricks in round");
    }

    @Test
    void get_number_of_result(){
        assertEquals(0, p.countOfResults(), "Count of Results should be 0");
        p.setResult(1, 0);
        assertEquals(1, p.countOfResults(), "Count of Results should be 1");
    }

    @Test
    void add_invalid_result(){
        p.setResult(-1, 0);
        assertEquals(0, p.countOfResults(), "Count of Results should be 0");
        p.setResult(2, 0);
        assertEquals(0, p.countOfResults(), "Result cannot exceed tricks in round");
    }

    @Test
    void get_empty_score(){
        assertEquals(-1, p.getScore(0), "Empty Score should be -1");
    }

    @Test
    void invalid_rounds_do_not_crash(){
        assertEquals(-1, p.getPrediction(-1));
        assertEquals(-1, p.getResult(-1));
        assertEquals(-1, p.getScore(-1));
        assertEquals(-1, p.getScore(2));
    }

    @Test
    void changing_or_clearing_input_invalidates_score() {
        assertTrue(p.setPrediction(1, 0));
        assertTrue(p.setResult(1, 0));
        assertEquals(30, p.getScore(0));

        assertTrue(p.setPrediction(0, 0));
        assertEquals(-10, p.getScore(0));

        p.clearResult(0);
        assertEquals(-1, p.getResult(0));
        assertEquals(-1, p.getScore(0));

        p.clearPrediction(0);
        assertEquals(-1, p.getPrediction(0));
    }

    @Test
    void setters_report_rejected_values() {
        assertFalse(p.setPrediction(-1, 0));
        assertFalse(p.setPrediction(2, 0));
        assertFalse(p.setPrediction(0, -1));
        assertFalse(p.setResult(-1, 0));
        assertFalse(p.setResult(2, 0));
        assertFalse(p.setResult(0, 2));
    }
}
