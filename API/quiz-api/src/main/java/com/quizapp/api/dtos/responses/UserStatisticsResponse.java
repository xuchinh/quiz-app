package com.quizapp.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatisticsResponse {
    private Integer userId;
    private String name;
    private String email;
    private Integer totalAttempts;
    private Integer totalCorrectAnswers;
    private Integer averageScore;
    private Double highestScore;
    private Double lowestScore;
}
