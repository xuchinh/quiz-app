package com.quizapp.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDetailResponse {
    private Integer questionId;
    private String questionText;
    private Integer selectedOptionId;
    private String selectedOptionText;
    private Integer correctOptionId;
    private String correctOptionText;
    private Boolean isCorrect;
}
