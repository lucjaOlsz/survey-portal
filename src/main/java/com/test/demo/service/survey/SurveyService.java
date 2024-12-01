package com.test.demo.service.survey;

import com.test.demo.model.Survey;

import java.util.List;
import java.util.Optional;

public interface SurveyService {
    List<Survey> getSurveysByUserId(Long userId);
    Optional<Survey> findById(Long id);
    void saveSurvey(Survey survey);
    void submitSurvey(Survey survey);
}
