package com.example.NotesApp.auth;


public class AuthDtos {
    public record RegisterReq(String email, String password, String name) {}
    public record AuthResp(String accessToken, UserView user) {}
    public record LoginReq(String email, String password) {}
    public record UserView(String id, String email, String name) {}
}
