package com.example.quizapp.models;

public class AnswerDetail {
    private int questionId;
    private String questionText;
    private int selectedOptionId;
    private String selectedOptionText;
    private int correctOptionId;
    private String correctOptionText;
    private boolean isCorrect;

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public int getSelectedOptionId() { return selectedOptionId; }
    public void setSelectedOptionId(int selectedOptionId) { this.selectedOptionId = selectedOptionId; }
    public String getSelectedOptionText() { return selectedOptionText; }
    public void setSelectedOptionText(String selectedOptionText) { this.selectedOptionText = selectedOptionText; }
    public int getCorrectOptionId() { return correctOptionId; }
    public void setCorrectOptionId(int correctOptionId) { this.correctOptionId = correctOptionId; }
    public String getCorrectOptionText() { return correctOptionText; }
    public void setCorrectOptionText(String correctOptionText) { this.correctOptionText = correctOptionText; }
    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
}
