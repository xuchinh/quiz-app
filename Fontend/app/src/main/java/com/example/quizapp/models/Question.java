package com.example.quizapp.models;

import java.util.List;

public class Question {
    private int id;
    private String questionText;
    private List<Option> options;
    private int selectedOptionId = -1; // For tracking user selection

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public List<Option> getOptions() { return options; }
    public void setOptions(List<Option> options) { this.options = options; }
    public int getSelectedOptionId() { return selectedOptionId; }
    public void setSelectedOptionId(int selectedOptionId) { this.selectedOptionId = selectedOptionId; }
}
