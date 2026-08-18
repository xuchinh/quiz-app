package com.quizapp.api.services;

import com.quizapp.api.dtos.requests.SubmitExamRequest;
import com.quizapp.api.dtos.responses.AnswerDetailResponse;
import com.quizapp.api.dtos.responses.ExamResultResponse;
import com.quizapp.api.exceptions.BadRequestException;
import com.quizapp.api.exceptions.NotFoundException;
import com.quizapp.api.models.*;
import com.quizapp.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamAttemptService {

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ExamAttemptAnswerRepository examAttemptAnswerRepository;

    @Transactional
    public ExamResultResponse submitExam(User user, SubmitExamRequest request) {
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new NotFoundException("Exam not found"));

        // Create exam attempt
        ExamAttempt attempt = new ExamAttempt();
        attempt.setUser(user);
        attempt.setExam(exam);
        attempt.setStartedAt(LocalDateTime.now().minusMinutes(exam.getDurationMinutes()));
        attempt.setFinishedAt(LocalDateTime.now());

        int correctCount = 0;
        int wrongCount = 0;
        List<AnswerDetailResponse> answerDetails = new ArrayList<>();

        // Process each answer
        for (var answerReq : request.getAnswers()) {
            Question question = questionRepository.findById(answerReq.getQuestionId())
                    .orElseThrow(() -> new NotFoundException("Question not found"));

            Option selectedOption = optionRepository.findById(answerReq.getSelectedOptionId())
                    .orElseThrow(() -> new NotFoundException("Option not found"));

            boolean isCorrect = selectedOption.getIsCorrect();
            if (isCorrect) {
                correctCount++;
            } else {
                wrongCount++;
            }

            // Save answer
            ExamAttemptAnswer answer = new ExamAttemptAnswer();
            answer.setExamAttempt(attempt);
            answer.setQuestion(question);
            answer.setSelectedOption(selectedOption);
            answer.setIsCorrect(isCorrect);

            // Find correct option for response
            Option correctOption = question.getOptions().stream()
                    .filter(Option::getIsCorrect)
                    .findFirst()
                    .orElse(null);

            AnswerDetailResponse detail = new AnswerDetailResponse(
                    question.getId(),
                    question.getQuestionText(),
                    selectedOption.getId(),
                    selectedOption.getOptionText(),
                    correctOption != null ? correctOption.getId() : null,
                    correctOption != null ? correctOption.getOptionText() : null,
                    isCorrect
            );
            answerDetails.add(detail);

            if (attempt.getAnswers() == null) {
                attempt.setAnswers(new ArrayList<>());
            }
            attempt.getAnswers().add(answer);
        }

        // Calculate score
        int score = (correctCount * 100) / exam.getTotalQuestions();
        attempt.setScore(score);
        attempt.setCorrectCount(correctCount);
        attempt.setWrongCount(wrongCount);

        attempt = examAttemptRepository.save(attempt);

        return new ExamResultResponse(
                attempt.getId(),
                score,
                correctCount,
                wrongCount,
                exam.getTotalQuestions(),
                attempt.getStartedAt(),
                attempt.getFinishedAt(),
                answerDetails
        );
    }

    public List<ExamResultResponse> getUserAttempts(User user) {
        List<ExamAttempt> attempts = examAttemptRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        return attempts.stream()
                .map(this::convertToExamResultResponse)
                .toList();
    }

    public ExamResultResponse getAttemptDetail(User user, Integer attemptId) {
        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt not found"));

        if (!attempt.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Unauthorized access");
        }

        return convertToExamResultResponse(attempt);
    }

    private ExamResultResponse convertToExamResultResponse(ExamAttempt attempt) {
        List<AnswerDetailResponse> answerDetails = attempt.getAnswers().stream()
                .map(answer -> {
                    Option correctOption = answer.getQuestion().getOptions().stream()
                            .filter(Option::getIsCorrect)
                            .findFirst()
                            .orElse(null);

                    return new AnswerDetailResponse(
                            answer.getQuestion().getId(),
                            answer.getQuestion().getQuestionText(),
                            answer.getSelectedOption().getId(),
                            answer.getSelectedOption().getOptionText(),
                            correctOption != null ? correctOption.getId() : null,
                            correctOption != null ? correctOption.getOptionText() : null,
                            answer.getIsCorrect()
                    );
                })
                .toList();

        return new ExamResultResponse(
                attempt.getId(),
                attempt.getScore(),
                attempt.getCorrectCount(),
                attempt.getWrongCount(),
                attempt.getExam().getTotalQuestions(),
                attempt.getStartedAt(),
                attempt.getFinishedAt(),
                answerDetails
        );
    }
}
