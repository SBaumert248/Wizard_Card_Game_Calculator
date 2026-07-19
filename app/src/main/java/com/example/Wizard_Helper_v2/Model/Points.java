package com.example.Wizard_Helper_v2.Model;

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
        return arr != null && round >= 0 && round < arr.length;
    }

    public int getScore(int round){
        if (!this.validRound(round, this.scores)) {
            return -1;
        }
        this.calculate(round);
        if (this.scores[round] == null) {
            return -1;
        }
        return this.sumScore(round);
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

    public boolean setPrediction(int value, int round){
        if (!this.validRound(round, this.predictions) || value < 0 || value > round + 1){
            return false;
        }
        this.predictions[round] = value;
        this.scores[round] = null;
        return true;
    }

    public boolean setResult(int value, int round){
        if (!this.validRound(round, this.results) || value < 0 || value > round + 1){
            return false;
        }
        this.results[round] = value;
        this.scores[round] = null;
        return true;
    }

    public void clearPrediction(int round) {
        if (this.validRound(round, this.predictions)) {
            this.predictions[round] = null;
            this.scores[round] = null;
        }
    }

    public void clearResult(int round) {
        if (this.validRound(round, this.results)) {
            this.results[round] = null;
            this.scores[round] = null;
        }
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

    private int sumScore(int round){
        int value = 0;
        for (int index = 0; index <= round; index++) {
            Integer score = this.scores[index];
            if (score == null) {
                return -1;
            }
            value += score;
        }
        return value;
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

    public boolean isValidForRounds(int rounds) {
        if (rounds <= 0
                || this.predictions == null
                || this.results == null
                || this.scores == null
                || this.predictions.length != rounds
                || this.results.length != rounds
                || this.scores.length != rounds) {
            return false;
        }

        for (int round = 0; round < rounds; round++) {
            Integer prediction = this.predictions[round];
            Integer result = this.results[round];
            Integer score = this.scores[round];
            int maxTricks = round + 1;

            if ((prediction != null && (prediction < 0 || prediction > maxTricks))
                    || (result != null && (result < 0 || result > maxTricks))) {
                return false;
            }

            if (prediction == null || result == null) {
                if (score != null) {
                    return false;
                }
            } else {
                int expectedScore = prediction.equals(result)
                        ? 20 + 10 * prediction
                        : -10 * Math.abs(prediction - result);
                if (score != null && score != expectedScore) {
                    return false;
                }
            }
        }
        return true;
    }
}
