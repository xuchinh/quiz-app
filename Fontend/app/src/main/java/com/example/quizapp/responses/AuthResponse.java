package com.example.quizapp.responses;

import com.example.quizapp.models.User;

public class AuthResponse {
    private String token;
    private User user;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
