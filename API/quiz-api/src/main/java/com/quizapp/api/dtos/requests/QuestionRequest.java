package com.quizapp.api.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    @NotNull(message = "Exam ID is required")
    private Integer examId;

    @NotBlank(message = "Question text is required")
    private String questionText;
}
