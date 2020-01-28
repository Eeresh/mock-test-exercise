package com.quiz.exception;

public class FailedToReadFileException extends RuntimeException {
    private String reason;

    public FailedToReadFileException(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}