package com.test.demo.exceptions;

public class SurveyAlreadySubmittedException extends RuntimeException {
    public SurveyAlreadySubmittedException(String message) {
        super(message);
    }
}
