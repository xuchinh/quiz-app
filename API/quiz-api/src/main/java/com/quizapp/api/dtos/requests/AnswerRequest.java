package com.quizapp.api.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {
    @NotNull(message = "Question ID is required")
    private Integer questionId;

    @NotNull(message = "Selected option ID is required")
    private Integer selectedOptionId;
}
