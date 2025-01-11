package com.example.Wizard_Helper_v2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.Wizard_Helper_v2.Model.Points;

class TestModelPoints {

    private final Points p = new Points();

    @Test
    void addition_isCorrect() {
        p.addPrediction(1);
        p.addResult(1);
        assertEquals(30, p.getScore(), "Score should return 30");
        p.addPrediction(1);
        p.addResult(1);
        assertEquals(60, p.getScore(), "Score should return 60");
    }

    @Test
    void addition_isCorrect_wrongOrder() {
        p.addResult(1);
        p.addPrediction(1);
        assertEquals(30, p.getScore(), "Score should return 30");
    }

    @Test
    void addition_isCorrect_2() {
        p.addPrediction(10);
        p.addResult(1);
        assertEquals( -90, p.getScore(), "Score should return -90");
        p.addPrediction(1);
        p.addResult(1);
        assertEquals( -60, p.getScore(), "Score should return -60");
    }

    @Test
    void addition_isCorrect_noResult() {
        p.addPrediction(0);
        p.addResult(0);
        assertEquals(20, p.getScore(), "Score should return 20");
    }

    @Test
    void score_after_prediction(){
        p.addPrediction(0);
        assertEquals(-1, p.getScore(), "Score should return -1");
    }

    @Test
    void score_before_prediction(){
        assertEquals(-1, p.getScore(), "Score should return -1");
    }

    @Test
    void get_number_of_prediction(){
        assertEquals(0, p.countOfPredictions(), "Count of Prediction should be 0");
        p.addPrediction(1);
        assertEquals(1, p.countOfPredictions(), "Count of Prediction should be 1");
    }

    @Test
    void add_invalid_prediction(){
        p.addPrediction(-1);
        assertEquals(0, p.countOfPredictions(), "Count of Prediction should be 0");
    }

    @Test
    void get_number_of_result(){
        assertEquals(0, p.countOfResults(), "Count of Results should be 0");
        p.addResult(1);
        assertEquals(1, p.countOfResults(), "Count of Results should be 1");
    }

    @Test
    void add_invalid_result(){
        p.addResult(-1);
        assertEquals(0, p.countOfResults(), "Count of Results should be 0");
    }

    @Test
    void get_empty_score(){
        assertEquals(-1, p.getScore(), "Empty Score should be -1");
    }
}