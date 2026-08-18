package com.quizapp.api.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequest {
    @NotNull(message = "Question ID is required")
    private Integer questionId;

    @NotBlank(message = "Option text is required")
    private String optionText;

    @NotNull(message = "Is correct flag is required")
    private Boolean isCorrect;
}
