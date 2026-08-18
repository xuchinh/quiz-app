package com.example.quizapp.responses;

import com.example.quizapp.models.AnswerDetail;
import java.util.List;

public class ExamResultResponse {
    private int attemptId;
    private int score;
    private int correctCount;
    private int wrongCount;
    private int totalQuestions;
    private String startedAt;
    private String finishedAt;
    private List<AnswerDetail> answerDetails;

    public int getAttemptId() { return attemptId; }
    public void setAttemptId(int attemptId) { this.attemptId = attemptId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getCorrectCount() { return correctCount; }
    public void setCorrectCount(int correctCount) { this.correctCount = correctCount; }
    public int getWrongCount() { return wrongCount; }
    public void setWrongCount(int wrongCount) { this.wrongCount = wrongCount; }
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    public String getStartedAt() { return startedAt; }
    public void setStartedAt(String startedAt) { this.startedAt = startedAt; }
    public String getFinishedAt() { return finishedAt; }
    public void setFinishedAt(String finishedAt) { this.finishedAt = finishedAt; }
    public List<AnswerDetail> getAnswerDetails() { return answerDetails; }
    public void setAnswerDetails(List<AnswerDetail> answerDetails) { this.answerDetails = answerDetails; }
}
