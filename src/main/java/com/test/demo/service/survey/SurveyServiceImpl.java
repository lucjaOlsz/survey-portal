package com.test.demo.service.survey;

import com.test.demo.exceptions.SurveyAlreadySubmittedException;
import com.test.demo.model.Survey;
import com.test.demo.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    public SurveyServiceImpl(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Override
    public List<Survey> getSurveysByUserId(Long userId) {
        // Pobiera listę ankiet dla danego użytkownika
        return surveyRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<Survey> findById(Long id) {
        // Pobiera ankietę na podstawie jej ID
        return surveyRepository.findById(id);
    }

    @Override
    public void saveSurvey(Survey survey) {
        if (!survey.isSubmitted()) {
            // Zapisuje ankietę tylko wtedy, gdy nie została jeszcze wysłana
            surveyRepository.save(survey);
        } else {
            throw new SurveyAlreadySubmittedException("Survey already submitted");
        }
    }

    @Override
    public void submitSurvey(Survey survey) {
        // Zaznacza ankietę jako wysłaną i zapisuje ją
        survey.setSubmitted(true);
        surveyRepository.save(survey);
    }
}
