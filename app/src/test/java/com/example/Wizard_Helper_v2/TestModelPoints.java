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
        p.setPrediction(10, 0);
        p.setResult(1, 0);
        assertEquals( -90, p.getScore(0), "Score should return -90");
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
    }

    @Test
    void get_empty_score(){
        assertEquals(-1, p.getScore(0), "Empty Score should be -1");
    }
}