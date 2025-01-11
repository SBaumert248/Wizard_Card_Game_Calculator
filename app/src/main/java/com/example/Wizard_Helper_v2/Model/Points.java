package com.example.Wizard_Helper_v2.Model;

import java.util.ArrayList;

public class Points {
    private final ArrayList<Integer> predictions;
    private final ArrayList<Integer> results;
    private final ArrayList<Integer> scores;

    public Points(){
        this.predictions = new ArrayList<>();
        this.results = new ArrayList<>();
        this.scores = new ArrayList<>();
    }

    public int getScore(){
        if (this.countOfPredictions() != this.countOfResults()) {
            return -1;
        } else {
            // entferne das zuletzt berechnete Ergebnis, weil es neu berechnet wird
            if (this.scores.size() == this.countOfPredictions()
                    && this.scores.size() == this.countOfResults()){
                this.removeLast(this.scores);
            }
            // calculate und add score
            this.calculate();
            if (!this.scores.isEmpty()){
                return this.sumScore();
            }
            return -1;
        }
    }

    public int getScoreOfRound(int round){
        int max = round;
        if (round > this.countOfScore()){
            max = this.countOfScore();
        }
        int value = 0;
        for (int idx = 0; idx < max; idx++){
            value += this.scores.get(idx);
        }
        return value;
    }

    public int countOfScore(){
        return this.scores.size();
    }

    public void addPrediction(int value){
        if (value < 0){
            return;
        }
        this.predictions.add(value);
    }

    public void removeLastPrediction(){
        if (this.predictions.isEmpty()){
            return;
        }
        this.removeLast(this.predictions);
    }

    public void addResult(int value){
        if (value < 0){
            return;
        }
        this.results.add(value);
    }

    public void removeLastResult(){
        if (this.results.isEmpty()){
            return;
        }
        this.removeLast(this.results);
    }

    public int countOfPredictions(){
        return this.predictions.size();
    }

    public int countOfResults(){
        return this.results.size();
    }

    private void removeLast(ArrayList<Integer> list){
        if (!list.isEmpty()){
            list.remove(list.size() - 1);
        }
    }

    private int sumScore(){
        int value = 0;
        for(int score: this.scores){
            value += score;
        }
        return value;
    }

    public int getPrediction(){
        if (this.predictions.isEmpty()) {
            return -1;
        } else {
            return this.predictions.get(this.countOfPredictions() - 1);
        }
    }

    public int getResult(){
        if (this.results.isEmpty()) {
            return -1;
        } else {
            return this.results.get(this.countOfResults() - 1);
        }
    }

    private void calculate(){
        int prediction = this.getPrediction();
        int result = this.getResult();
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
        this.scores.add(value);
    }

}
