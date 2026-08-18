package com.quizapp.api.services;

import com.quizapp.api.dtos.requests.OptionRequest;
import com.quizapp.api.dtos.responses.OptionResponse;
import com.quizapp.api.exceptions.NotFoundException;
import com.quizapp.api.models.Option;
import com.quizapp.api.models.Question;
import com.quizapp.api.repositories.OptionRepository;
import com.quizapp.api.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public OptionResponse getOptionById(Integer optionId) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new NotFoundException("Option not found"));
        return convertToOptionResponse(option);
    }

    public List<OptionResponse> getOptionsByQuestion(Integer questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new NotFoundException("Question not found");
        }
        return optionRepository.findByQuestionId(questionId).stream()
                .map(this::convertToOptionResponse)
                .collect(Collectors.toList());
    }

    public OptionResponse createOption(OptionRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new NotFoundException("Question not found"));

        Option option = new Option();
        option.setQuestion(question);
        option.setOptionText(request.getOptionText());
        option.setIsCorrect(request.getIsCorrect());

        option = optionRepository.save(option);
        return convertToOptionResponse(option);
    }

    public OptionResponse updateOption(Integer optionId, OptionRequest request) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new NotFoundException("Option not found"));

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new NotFoundException("Question not found"));

        option.setQuestion(question);
        option.setOptionText(request.getOptionText());
        option.setIsCorrect(request.getIsCorrect());

        option = optionRepository.save(option);
        return convertToOptionResponse(option);
    }

    public void deleteOption(Integer optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new NotFoundException("Option not found");
        }
        optionRepository.deleteById(optionId);
    }

    private OptionResponse convertToOptionResponse(Option option) {
        return new OptionResponse(
                option.getId(),
                option.getOptionText(),
                option.getIsCorrect()
        );
    }
}
