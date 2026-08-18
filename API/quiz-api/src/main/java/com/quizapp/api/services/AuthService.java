package com.quizapp.api.services;

import com.quizapp.api.dtos.requests.LoginRequest;
import com.quizapp.api.dtos.requests.RegisterRequest;
import com.quizapp.api.dtos.responses.AuthResponse;
import com.quizapp.api.dtos.responses.UserResponse;
import com.quizapp.api.exceptions.BadRequestException;
import com.quizapp.api.models.User;
import com.quizapp.api.repositories.UserRepository;
import com.quizapp.api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        UserResponse userResponse = new UserResponse(user.getId(), user.getName(), user.getEmail());

        return new AuthResponse(token, userResponse);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        UserResponse userResponse = new UserResponse(user.getId(), user.getName(), user.getEmail());

        return new AuthResponse(token, userResponse);
    }
}
