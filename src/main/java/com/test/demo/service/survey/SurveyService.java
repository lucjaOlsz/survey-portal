package com.test.demo.service.survey;

import com.test.demo.model.Survey;
import com.test.demo.model.User;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SurveyService {
    Map<YearMonth, List<Survey>> getSurveysGroupedByMonth(Long userId);
    Optional<Survey> getSurveyById(Long id);
    boolean isSurveyOwner(Long surveyId, String userEmail);
    Survey createSurvey(User user);
    void saveSurvey(Survey survey);
    void submitSurvey(Survey survey);
    List<Survey> getAllSurveysSubmitted();

}
