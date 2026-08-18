package com.quizapp.api.controllers;

import com.quizapp.api.dtos.responses.UserResponse;
import com.quizapp.api.dtos.responses.UserStatisticsResponse;
import com.quizapp.api.dtos.responses.UserDetailedStatisticsResponse;
import com.quizapp.api.models.User;
import com.quizapp.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserProfile(user));
    }

    @GetMapping("/statistics")
    public ResponseEntity<UserStatisticsResponse> getStatistics(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserStatistics(user));
    }

    @GetMapping("/statistics/detailed")
    public ResponseEntity<UserDetailedStatisticsResponse> getDetailedStatistics(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserDetailedStatistics(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(userService.getUserProfile(user));
    }

    @GetMapping("/{userId}/statistics")
    public ResponseEntity<UserStatisticsResponse> getUserStatistics(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(userService.getUserStatistics(user));
    }

    @GetMapping("/{userId}/statistics/detailed")
    public ResponseEntity<UserDetailedStatisticsResponse> getUserDetailedStatistics(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(userService.getUserDetailedStatistics(user));
    }
}
