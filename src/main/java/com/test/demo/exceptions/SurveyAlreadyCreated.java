package com.test.demo.exceptions;

public class SurveyAlreadyCreated extends RuntimeException {
    public SurveyAlreadyCreated(String message) {
        super(message);
    }
}
