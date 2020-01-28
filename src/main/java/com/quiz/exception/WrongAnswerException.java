package com.quiz.exception;

public class WrongAnswerException extends RuntimeException {
    private String explanation;
    private float scoredPercentage;

    public WrongAnswerException(String explanation, float scoredPercentage) {
        this.explanation = explanation;
        this.scoredPercentage = scoredPercentage;
    }

    public String getExplanation() {
        return explanation;
    }

    public float getScoredPercentage() {
        return scoredPercentage;
    }
}
