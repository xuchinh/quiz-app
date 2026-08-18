package com.quizapp.api.services;

import com.quizapp.api.dtos.requests.ExamRequest;
import com.quizapp.api.dtos.responses.*;
import com.quizapp.api.exceptions.NotFoundException;
import com.quizapp.api.models.*;
import com.quizapp.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<ExamResponse> getAllExams() {
        return examRepository.findAll().stream()
                .map(this::convertToExamResponse)
                .collect(Collectors.toList());
    }

    public List<ExamResponse> getExamsBySubject(Integer subjectId) {
        if (!subjectRepository.existsById(subjectId)) {
            throw new NotFoundException("Subject not found");
        }
        return examRepository.findBySubjectId(subjectId).stream()
                .map(this::convertToExamResponse)
                .collect(Collectors.toList());
    }

    public ExamDetailResponse getExamById(Integer examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new NotFoundException("Exam not found"));

        return convertToExamDetailResponse(exam);
    }

    public ExamResponse createExam(ExamRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new NotFoundException("Subject not found"));

        Exam exam = new Exam();
        exam.setSubject(subject);
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setTotalQuestions(request.getTotalQuestions());

        exam = examRepository.save(exam);
        return convertToExamResponse(exam);
    }

    public ExamResponse updateExam(Integer examId, ExamRequest request) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new NotFoundException("Exam not found"));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new NotFoundException("Subject not found"));

        exam.setSubject(subject);
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setTotalQuestions(request.getTotalQuestions());

        exam = examRepository.save(exam);
        return convertToExamResponse(exam);
    }

    public void deleteExam(Integer examId) {
        if (!examRepository.existsById(examId)) {
            throw new NotFoundException("Exam not found");
        }
        examRepository.deleteById(examId);
    }

    public List<ExamResponse> searchExams(String keyword) {
        return examRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword).stream()
                .map(this::convertToExamResponse)
                .collect(Collectors.toList());
    }

    private ExamResponse convertToExamResponse(Exam exam) {
        return new ExamResponse(
                exam.getId(),
                exam.getTitle(),
                exam.getDescription(),
                exam.getDurationMinutes(),
                exam.getTotalQuestions(),
                exam.getSubject().getName(),
                exam.getSubject().getId(),
                exam.getCreatedAt()
        );
    }

    private ExamDetailResponse convertToExamDetailResponse(Exam exam) {
        List<QuestionResponse> questions = exam.getQuestions().stream()
                .map(this::convertToQuestionResponse)
                .collect(Collectors.toList());

        return new ExamDetailResponse(
                exam.getId(),
                exam.getTitle(),
                exam.getDescription(),
                exam.getDurationMinutes(),
                exam.getTotalQuestions(),
                exam.getSubject().getName(),
                questions
        );
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