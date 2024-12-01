package com.test.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
@Table(name = "surveys")
public class Survey {
    @Id
    @Column(name = "survey_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacja "wiele do jednego" z encją User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Question 1: Satisfaction with overall quality
    @Min(1)
    @Max(10)
    @Column(name = "quality_satisfaction_rate")
    private int qualitySatisfactionRate = 10;

    // Question 2: Professionalism of the cleaning team
    @Min(1)
    @Max(5)
    @Column(name = "professionalism_rate")
    private int professionalismRate = 5;

    // Open-ended comment for professionalism (optional)
    @Column(name = "professionalism_comment")
    private String professionalismComment;

    // Question 3: Were services completed on time? (Yes/No)
    @Column(name = "completed_on_time")
    private boolean completedOnTime;

    // Optional comment for timing
    @Column(name = "time_comment")
    private String timeComment;

    // Question 4: How well did the team handle high-traffic areas
    @Min(1)
    @Max(10)
    @Column(name = "high_traffic_handling_rate")
    private int highTrafficHandlingRate = 10;

    // Open-ended comment for handling high-traffic areas
    @Column(name = "high_traffic_comment")
    private String highTrafficComment;

    // Question 5: Did the team effectively communicate? (Yes/No)
    @Column(name = "communication_effective")
    private boolean communicationEffective;

    // Comment for communication
    @Column(name = "communication_comment")
    private String communicationComment;

    // Question 6: Satisfaction with products and equipment
    @Min(1)
    @Max(5)
    @Column(name = "products_satisfaction_rate")
    private int productsSatisfactionRate = 5;

    // Question 7: Areas that need more frequent or deeper cleaning (Open-ended)
    @Column(name = "areas_for_improvement")
    private String areasForImprovement;

    // Question 8: Would you recommend our services? (Yes/No)
    @Column(name = "recommend")
    private boolean recommend;

    // Open-ended comment for recommendation
    @Column(name = "recommendation_comment")
    private String recommendationComment;

    @Column(name = "is_submitted")
    private boolean isSubmitted = false;

    public Object getDescription() {
    }

    public Long getTitle() {
    }
}
