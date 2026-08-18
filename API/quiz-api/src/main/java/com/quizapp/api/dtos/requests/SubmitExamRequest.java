package com.quizapp.api.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitExamRequest {
    @NotNull(message = "Exam ID is required")
    private Integer examId;

    @NotEmpty(message = "Answers cannot be empty")
    @Valid
    private List<AnswerRequest> answers;
}