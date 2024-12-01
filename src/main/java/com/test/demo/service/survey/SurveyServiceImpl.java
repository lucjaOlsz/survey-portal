package com.test.demo.service.survey;

import com.test.demo.exceptions.SurveyAlreadyCreated;
import com.test.demo.exceptions.SurveyAlreadySubmittedException;
import com.test.demo.model.Survey;
import com.test.demo.model.User;
import com.test.demo.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepository surveyRepository;

    public SurveyServiceImpl(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Override
    public Map<YearMonth, List<Survey>> getSurveysGroupedByMonth(Long userId) {
        List<Survey> surveys = surveyRepository.findAllByUserId(userId);
        return surveys.stream()
                .filter(survey -> survey.getCreatedAt() != null)
                .collect(Collectors.groupingBy(survey -> YearMonth.from(survey.getCreatedAt())));
    }

    @Override
    public Optional<Survey> getSurveyById(Long id) {
        return surveyRepository.findById(id);
    }

    @Override
    public boolean isSurveyOwner(Long surveyId, String userEmail) {
        return surveyRepository.findById(surveyId)
                .map(survey -> survey.getUser().getEmail().equals(userEmail))
                .orElse(false);
    }

    @Override
    public Survey createSurvey(User user) {
        LocalDate startOfMonth = YearMonth.now().atDay(1);
        LocalDate endOfMonth = YearMonth.now().atEndOfMonth();

        Optional<Survey> lastSurvey = surveyRepository.findFirstByUserIdAndCreatedAtBetween(user.getId(), startOfMonth.atStartOfDay(), endOfMonth.atStartOfDay());

        if (lastSurvey.isPresent()) {
            throw new SurveyAlreadyCreated("Survey already exists for this month");
        } else {
            Survey survey = new Survey();
            survey.setUser(user);
            survey.setCreatedAt(LocalDateTime.now());
            surveyRepository.save(survey);
            return survey;
        }
    }

    @Override
    public void saveSurvey(Survey survey) {
        // Zapisuje ankietę tylko wtedy, gdy nie została jeszcze wysłana
        if (survey.isSubmitted()) {
            throw new SurveyAlreadySubmittedException("Survey already submitted");
        }
        surveyRepository.save(survey);
    }

    @Override
    public void submitSurvey(Survey survey) {
        // Zaznacza ankietę jako wysłaną i zapisuje ją
        /// TODO: refactor these errors
        if (survey.isSubmitted()) {
            throw new SurveyAlreadySubmittedException("Survey already submitted");
        }
        survey.setSubmitted(true);
        survey.setSubmittedAt(LocalDateTime.now());
        surveyRepository.save(survey);
    }
}
