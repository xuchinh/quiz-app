package com.quizapp.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailedStatisticsResponse {
    private Integer userId;
    private String name;
    private String email;
    
    // Overall statistics
    private Integer totalAttempts;
    private Integer totalCorrectAnswers;
    private Integer totalWrongAnswers;
    private Integer totalQuestions;
    private Double accuracy;
    
    // Score statistics
    private Integer averageScore;
    private Double highestScore;
    private Double lowestScore;
    
    // Performance by subject
    private java.util.List<SubjectPerformance> subjectPerformance;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectPerformance {
        private Integer subjectId;
        private String subjectName;
        private Integer attempts;
        private Integer correctAnswers;
        private Integer totalQuestions;
        private Double accuracy;
        private Integer averageScore;
    }
}
