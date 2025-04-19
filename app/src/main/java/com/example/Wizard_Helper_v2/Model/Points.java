package com.example.Wizard_Helper_v2.Model;

import java.util.ArrayList;

public class Points {
    private final Integer[] predictions;
    private final Integer[] results;
    private final Integer[] scores;

    public Points(int round){
        this.predictions = new Integer[round];
        this.results = new Integer[round];
        this.scores = new Integer[round];
    }

    private boolean isEmpty(Integer[] arr){
        if (arr == null || arr.length == 0) {
            return true; // Null-Referenz oder leeres Array gilt als "leer"
        }
        for (Integer value : arr) {
            if (value != null) {
                return false; // Sobald ein nicht-null-Wert gefunden wird, ist das Array nicht leer
            }
        }
        return true; // Alle Elemente waren null
    }

    private boolean validRound(int round, Integer[] arr){
        return arr.length > 0 && arr.length > round;
    }

    public int getScore(int round){
        // calculate und add score
        this.calculate(round);
        if (!this.isEmpty(this.scores) || this.scores[round] != null) {
            return this.sumScore(round);
        }
        return -1;
    }
    public int countOfScore(){
        int count = 0;
        for (int idx = 0; idx < this.scores.length; idx++){
            if (this.scores[idx] != null){
                count++;
            }
        }
        return count;
    }

    public void setPrediction(int value, int round){
        if ((value < 0) || !this.validRound(round, this.predictions)){
            return;
        }
        if (round >= this.predictions.length){
            return;
        }
        this.predictions[round] = value;
    }

    public void setResult(int value, int round){
        if ((value < 0) || !this.validRound(round, this.results)){
            return;
        }
        if (round >= this.results.length){
            return;
        }
        this.results[round] = value;
    }

    public int countNotNullValues(Integer[] arr){
        int count = 0;
        for (Integer prediction : arr) {
            if (prediction != null) {
                count++;
            }
        }
        return count;
    }

    public int countOfPredictions(){
        return this.countNotNullValues(this.predictions);
    }

    public int countOfResults(){
        return this.countNotNullValues(this.results);
    }

    private void removeLast(ArrayList<Integer> list){
        if (!list.isEmpty()){
            list.remove(list.size() - 1);
        }
    }

    private int sumScore(int round){
        int value = 0;
        int count = 0;
        for(Integer score: this.scores){
            if (score != null && count < round+1) {
                value += score;
                count++;
            }
        }
        // Wenn die Anzahl der Ergebnisse mit der Rundenanzahl übereinstimmt
        if (count == round+1){
            return value;
        } else {
            return -1;
        }
    }

    public int getPrediction(int round){
        if (this.isEmpty(this.predictions) || !this.validRound(round, this.predictions)) {
            return -1;
        } else if (this.predictions[round] == null){
            return -1;
        } else {
            return this.predictions[round];
        }
    }

    public int getResult(int round){
        if (this.isEmpty(this.results) || !this.validRound(round, this.results)) {
            return -1;
        } else if (this.results[round] == null){
            return -1;
        }  else {
            return this.results[round];
        }
    }

    private void calculate(int round){
        int prediction = this.getPrediction(round);
        int result = this.getResult(round);
        if (prediction == -1 || result == -1){
            return;
        }
        int value;
        // Wizard-Punkte-Regeln
        if (prediction == result) {
            value = 20 + 10 * prediction;
        } else {
            value = -10 * Math.abs(prediction - result);
        }
        this.scores[round] = value;
    }

}
