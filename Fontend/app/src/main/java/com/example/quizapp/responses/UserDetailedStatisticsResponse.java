package com.example.quizapp.responses;

import java.util.List;

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
    private List<SubjectPerformance> subjectPerformance;
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Integer getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(Integer totalAttempts) { this.totalAttempts = totalAttempts; }
    
    public Integer getTotalCorrectAnswers() { return totalCorrectAnswers; }
    public void setTotalCorrectAnswers(Integer totalCorrectAnswers) { this.totalCorrectAnswers = totalCorrectAnswers; }
    
    public Integer getTotalWrongAnswers() { return totalWrongAnswers; }
    public void setTotalWrongAnswers(Integer totalWrongAnswers) { this.totalWrongAnswers = totalWrongAnswers; }
    
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public Double getAccuracy() { return accuracy; }
    public void setAccuracy(Double accuracy) { this.accuracy = accuracy; }
    
    public Integer getAverageScore() { return averageScore; }
    public void setAverageScore(Integer averageScore) { this.averageScore = averageScore; }
    
    public Double getHighestScore() { return highestScore; }
    public void setHighestScore(Double highestScore) { this.highestScore = highestScore; }
    
    public Double getLowestScore() { return lowestScore; }
    public void setLowestScore(Double lowestScore) { this.lowestScore = lowestScore; }
    
    public List<SubjectPerformance> getSubjectPerformance() { return subjectPerformance; }
    public void setSubjectPerformance(List<SubjectPerformance> subjectPerformance) { this.subjectPerformance = subjectPerformance; }
    
    public static class SubjectPerformance {
        private Integer subjectId;
        private String subjectName;
        private Integer attempts;
        private Integer correctAnswers;
        private Integer totalQuestions;
        private Double accuracy;
        private Integer averageScore;
        
        public Integer getSubjectId() { return subjectId; }
        public void setSubjectId(Integer subjectId) { this.subjectId = subjectId; }
        
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        
        public Integer getAttempts() { return attempts; }
        public void setAttempts(Integer attempts) { this.attempts = attempts; }
        
        public Integer getCorrectAnswers() { return correctAnswers; }
        public void setCorrectAnswers(Integer correctAnswers) { this.correctAnswers = correctAnswers; }
        
        public Integer getTotalQuestions() { return totalQuestions; }
        public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
        
        public Double getAccuracy() { return accuracy; }
        public void setAccuracy(Double accuracy) { this.accuracy = accuracy; }
        
        public Integer getAverageScore() { return averageScore; }
        public void setAverageScore(Integer averageScore) { this.averageScore = averageScore; }
    }
}
