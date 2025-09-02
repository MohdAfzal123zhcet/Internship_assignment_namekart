package com.example.NotesApp.auth;

import com.example.NotesApp.config.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth") @CrossOrigin
public class AuthController {
    private final AppUserService users; private final AuthenticationManager am; private final JwtService jwt;
    public AuthController(AppUserService users, AuthenticationManager am, JwtService jwt){ this.users=users; this.am=am; this.jwt=jwt; }


    @PostMapping("/register")
    public ResponseEntity<AuthDtos.UserView> register(@RequestBody @Valid AuthDtos.RegisterReq req){
        var u=users.register(req.email(), req.name(), req.password());
        return ResponseEntity.status(201).body(new AuthDtos.UserView(u.getId(), u.getEmail(), u.getName()));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthResp> login(@RequestBody @Valid AuthDtos.LoginReq req){
        am.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        var u=users.loadUserByUsername(req.email());
        String token=jwt.generateToken(u.getUsername());
        return ResponseEntity.ok(new AuthDtos.AuthResp(token, new AuthDtos.UserView(((AppUser)u).getId(), u.getUsername(), ((AppUser)u).getName())));
    }
}
