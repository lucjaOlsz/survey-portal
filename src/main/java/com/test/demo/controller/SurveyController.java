package com.test.demo.controller;

import com.test.demo.model.Survey;
import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;
import com.test.demo.service.survey.SurveyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyServiceImpl surveyService;
    private final UserRepository userRepository;

    @Autowired
    public SurveyController(SurveyServiceImpl surveyService, UserRepository userRepository) {
        this.surveyService = surveyService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getUserSurveys(Authentication authentication, Model model) {
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow();
        Map<YearMonth, List<Survey>> surveysByMonth = surveyService.getSurveysGroupedByMonth(currentUser.getId());
        model.addAttribute("surveysByMonth", surveysByMonth);
        return "surveyList";
    }

    @PostMapping("/create")
    public String createSurvey(Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        /// TODO: Add message if survey already exists
        try {
            Survey newSurvey = surveyService.createSurvey(user);
            return "redirect:/survey/" + newSurvey.getId();
        } catch (Exception e) {
            return "redirect:/survey?error=already-created";
        }
    }

    // Metoda GET - wyświetlanie ankiety (bez zmian)
    @GetMapping("/survey/{id}")
    @PreAuthorize("@surveyServiceImpl.isSurveyOwner(#id, authentication.name)")
    public String getSurvey(@PathVariable Long id, Authentication authentication, Model model) {
        Survey survey = surveyService.getSurveyById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));
        model.addAttribute("survey", survey);
        return "surveyDetail";
    }


    // Metoda PATCH - edytowanie ankiety
    @PatchMapping("/{survey}")
    @PreAuthorize("@surveyServiceImpl.isSurveyOwner(#survey.id, authentication.name)")
    public String updateSurvey(@PathVariable Survey survey, @ModelAttribute Survey updatedSurvey, Authentication authentication, BindingResult bindingResult) {
        Long id = survey.getId();

        // Sprawdzamy, czy ankieta została już wysłana
        if (survey.isSubmitted()) {
            /// TODO: refactor these query params
            return "redirect:/survey/" + id + "?error=already-submitted";
        }

        // Jeśli formularz zawiera błędy, przekieruj z powrotem do formularza
        if (bindingResult.hasErrors()) {
            return "redirect:/survey/" + id + "?error=form-validation";
        }

        // Aktualizacja ankiety na podstawie danych z formularza
        survey.setQualitySatisfactionRate(updatedSurvey.getQualitySatisfactionRate());
        survey.setProfessionalismRate(updatedSurvey.getProfessionalismRate());
        survey.setProfessionalismComment(updatedSurvey.getProfessionalismComment());
        survey.setCompletedOnTime(updatedSurvey.isCompletedOnTime());
        survey.setTimeComment(updatedSurvey.getTimeComment());
        survey.setHighTrafficHandlingRate(updatedSurvey.getHighTrafficHandlingRate());
        survey.setHighTrafficComment(updatedSurvey.getHighTrafficComment());
        survey.setCommunicationEffective(updatedSurvey.isCommunicationEffective());
        survey.setCommunicationComment(updatedSurvey.getCommunicationComment());
        survey.setProductsSatisfactionRate(updatedSurvey.getProductsSatisfactionRate());
        survey.setAreasForImprovement(updatedSurvey.getAreasForImprovement());
        survey.setRecommend(updatedSurvey.isRecommend());
        survey.setRecommendationComment(updatedSurvey.getRecommendationComment());

        // Zapisanie zaktualizowanej ankiety
        surveyService.saveSurvey(survey);

        // Po edytowaniu przekierowujemy do szczegółów ankiety
        return "redirect:/survey/" + id;
    }

}
