package com.example.quizapp.models;

public class Option {
    private int id;
    private String optionText;
    private Boolean isCorrect;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
}
