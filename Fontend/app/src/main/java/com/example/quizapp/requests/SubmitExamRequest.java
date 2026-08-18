package com.example.quizapp.requests;

import java.util.List;

public class SubmitExamRequest {
    private int examId;
    private List<AnswerRequest> answers;

    public SubmitExamRequest(int examId, List<AnswerRequest> answers) {
        this.examId = examId;
        this.answers = answers;
    }

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }
    public List<AnswerRequest> getAnswers() { return answers; }
    public void setAnswers(List<AnswerRequest> answers) { this.answers = answers; }
}
