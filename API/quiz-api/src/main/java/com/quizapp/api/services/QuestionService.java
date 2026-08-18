package com.quizapp.api.services;

import com.quizapp.api.dtos.requests.QuestionRequest;
import com.quizapp.api.dtos.responses.QuestionResponse;
import com.quizapp.api.dtos.responses.OptionResponse;
import com.quizapp.api.exceptions.NotFoundException;
import com.quizapp.api.models.Question;
import com.quizapp.api.models.Exam;
import com.quizapp.api.repositories.QuestionRepository;
import com.quizapp.api.repositories.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamRepository examRepository;

    public QuestionResponse getQuestionById(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found"));
        return convertToQuestionResponse(question);
    }

    public List<QuestionResponse> getQuestionsByExam(Integer examId) {
        if (!examRepository.existsById(examId)) {
            throw new NotFoundException("Exam not found");
        }
        return questionRepository.findByExamId(examId).stream()
                .map(this::convertToQuestionResponse)
                .collect(Collectors.toList());
    }

    public QuestionResponse createQuestion(QuestionRequest request) {
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new NotFoundException("Exam not found"));

        Question question = new Question();
        question.setExam(exam);
        question.setQuestionText(request.getQuestionText());

        question = questionRepository.save(question);
        return convertToQuestionResponse(question);
    }

    public QuestionResponse updateQuestion(Integer questionId, QuestionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found"));

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new NotFoundException("Exam not found"));

        question.setExam(exam);
        question.setQuestionText(request.getQuestionText());

        question = questionRepository.save(question);
        return convertToQuestionResponse(question);
    }

    public void deleteQuestion(Integer questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new NotFoundException("Question not found");
        }
        questionRepository.deleteById(questionId);
    }

    public List<QuestionResponse> searchQuestions(String keyword) {
        return questionRepository.findByQuestionTextContainingIgnoreCase(keyword).stream()
                .map(this::convertToQuestionResponse)
                .collect(Collectors.toList());
    }

    private QuestionResponse convertToQuestionResponse(Question question) {
        List<OptionResponse> options = question.getOptions().stream()
                .map(option -> new OptionResponse(option.getId(), option.getOptionText(), null))
                .collect(Collectors.toList());

        return new QuestionResponse(
                question.getId(),
                question.getQuestionText(),
                options
        );
    }
}
