package com.test.demo.repository;

import com.test.demo.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findAllByUserId(Long userId);  /// metoda do pobierania ankiety na podstawie id użytkownika
}
