package com.example.Wizard_Helper_v2.Model;

public class Player {
    private final String name;
    private final Points score;


    public Player(String name) {
        this.name = name;
        this.score = new Points();
    }

    public int getScore(){
        return this.score.getScore();
    }

    public int getScoreOfRound(int round){
        if (round < 1 || round > this.score.countOfScore() ){
            return -1;
        }
        return this.score.getScoreOfRound(round);
    }

    public void addPrediction(int value){
        this.score.addPrediction(value);
    }

    public int getPrediction(){
        return this.score.getPrediction();
    }

    public void removeLastPrecition(){
        this.score.removeLastPrediction();
    }

    public void addResult(int value){
        this.score.addResult(value);
    }

    public int getResult(){
        return this.score.getResult();
    }

    public void removeLastResult(){
        this.score.removeLastResult();
    }

    public String getName() {
        return this.name;
    }

    public boolean isDone(int roundNumber){
        return this.hasPredictionForRound(roundNumber) && this.hasResultForRound(roundNumber);
    }

    public boolean hasPredictionForRound(int roundNumber) {
        return this.score.countOfPredictions() == roundNumber;
    }

    public boolean hasResultForRound(int roundNumber) {
        return this.score.countOfResults() == roundNumber;
    }

}
