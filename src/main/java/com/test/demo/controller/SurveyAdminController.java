package com.test.demo.controller;

import com.test.demo.model.Survey;
import com.test.demo.service.survey.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class SurveyAdminController {
    private final SurveyService surveyService;
    @Autowired
    public SurveyAdminController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        List<Survey> surveys = surveyService.getAllSurveysSubmitted();

        // Calculate averages
        double avgQualitySatisfactionRate = surveys.stream()
                .mapToInt(Survey::getQualitySatisfactionRate)
                .average()
                .orElse(0);
        double avgProfessionalismRate = surveys.stream()
                .mapToInt(Survey::getProfessionalismRate)
                .average()
                .orElse(0);
        double avgHighTrafficHandlingRate = surveys.stream()
                .mapToInt(Survey::getHighTrafficHandlingRate)
                .average()
                .orElse(0);
        double avgProductsSatisfactionRate = surveys.stream()
                .mapToInt(Survey::getProductsSatisfactionRate)
                .average()
                .orElse(0);

        // Add averages and surveys to the model
        model.addAttribute("surveys", surveys);
        model.addAttribute("avgQualitySatisfactionRate", avgQualitySatisfactionRate);
        model.addAttribute("avgProfessionalismRate", avgProfessionalismRate);
        model.addAttribute("avgHighTrafficHandlingRate", avgHighTrafficHandlingRate);
        model.addAttribute("avgProductsSatisfactionRate", avgProductsSatisfactionRate);

        return "adminDashboard"; // Ensure the template file is named admin-dashboard.html
    }

}
