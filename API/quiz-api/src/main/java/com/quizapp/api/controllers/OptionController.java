package com.quizapp.api.controllers;

import com.quizapp.api.dtos.requests.OptionRequest;
import com.quizapp.api.dtos.responses.OptionResponse;
import com.quizapp.api.services.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    @Autowired
    private OptionService optionService;

    @GetMapping("/{optionId}")
    public ResponseEntity<OptionResponse> getOptionById(@PathVariable Integer optionId) {
        return ResponseEntity.ok(optionService.getOptionById(optionId));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<OptionResponse>> getOptionsByQuestion(@PathVariable Integer questionId) {
        return ResponseEntity.ok(optionService.getOptionsByQuestion(questionId));
    }

    @PostMapping
    public ResponseEntity<OptionResponse> createOption(@Valid @RequestBody OptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(optionService.createOption(request));
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<OptionResponse> updateOption(
            @PathVariable Integer optionId,
            @Valid @RequestBody OptionRequest request) {
        return ResponseEntity.ok(optionService.updateOption(optionId, request));
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Integer optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
