package com.test.demo.controller;

import com.test.demo.model.Survey;
import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;
import com.test.demo.service.survey.SurveyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    @ResponseBody
    public List<Survey> getUserSurveys(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("User not logged in");
        }

        String userEmail = principal.getName();
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow();

        return surveyService.getSurveysByUserId(currentUser.getId());
    }

    @PostMapping("/createSurvey")
    @ResponseBody
    public String createSurvey(@ModelAttribute Survey survey, Principal principal) {
        if (principal == null) {
            return "log in first";
        }

        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        survey.setUser(user);
        surveyService.saveSurvey(survey);

        return "Survey created successfully";
    }

    // Metoda GET - wyświetlanie ankiety (bez zmian)
    @GetMapping("/survey/{id}")
    public String getSurvey(@PathVariable Long id, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userEmail = principal.getName();
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow();

        Survey foundSurvey = surveyService.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        if (!foundSurvey.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/access-denied";
        }

        if (foundSurvey.isSubmitted()) {
            return "redirect:/survey/" + id + "?error=already-submitted";
        }

        Model survey = model.addAttribute("survey", foundSurvey);
        return "surveyDetail";
    }

    // Metoda PATCH - edytowanie ankiety
    @PatchMapping("/survey/{id}")
    public String updateSurvey(@PathVariable Long id,
                               @ModelAttribute Survey updatedSurvey,
                               Principal principal,
                               BindingResult bindingResult) {

        if (principal == null) {
            return "redirect:/login";
        }

        String userEmail = principal.getName();
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow();
        Survey existingSurvey = surveyService.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        // Sprawdzamy, czy użytkownik jest właścicielem ankiety
        if (!existingSurvey.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/access-denied";
        }

        // Sprawdzamy, czy ankieta została już wysłana
        if (existingSurvey.isSubmitted()) {
            return "redirect:/survey/" + id + "?error=already-submitted";
        }

        // Jeśli formularz zawiera błędy, przekieruj z powrotem do formularza
        if (bindingResult.hasErrors()) {
            return "redirect:/survey/" + id + "?error=form-validation";
        }

        // Aktualizacja ankiety na podstawie danych z formularza
        existingSurvey.setProfessionalismComment(updatedSurvey.getProfessionalismComment());
        existingSurvey.setTimeComment(updatedSurvey.getTimeComment());
        existingSurvey.setHighTrafficComment(updatedSurvey.getHighTrafficComment());
        existingSurvey.setCommunicationComment(updatedSurvey.getCommunicationComment());
        existingSurvey.setAreasForImprovement(updatedSurvey.getAreasForImprovement());
        existingSurvey.setRecommendationComment(updatedSurvey.getRecommendationComment());

        // Zapisanie zaktualizowanej ankiety
        surveyService.saveSurvey(existingSurvey);

        // Po edytowaniu przekierowujemy do szczegółów ankiety
        return "redirect:/survey/" + id;
    }

}
