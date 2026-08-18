package com.example.quizapp.requests;

public class AnswerRequest {
    private int questionId;
    private int selectedOptionId;

    public AnswerRequest(int questionId, int selectedOptionId) {
        this.questionId = questionId;
        this.selectedOptionId = selectedOptionId;
    }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public int getSelectedOptionId() { return selectedOptionId; }
    public void setSelectedOptionId(int selectedOptionId) { this.selectedOptionId = selectedOptionId; }
}
