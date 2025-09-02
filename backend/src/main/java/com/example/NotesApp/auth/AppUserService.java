package com.example.NotesApp.auth;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository repo;
    private final PasswordEncoder encoder;
    public AppUserService(AppUserRepository repo, PasswordEncoder encoder)
    { this.repo=repo; this.encoder=encoder;
    }
    public AppUser register(String email, String name, String rawPwd){
        if(repo.findByEmail(email).isPresent()) throw new IllegalArgumentException("Email already used");
        AppUser u=new AppUser();
        u.setEmail(email);
        u.setName(name);
        u.setPasswordHash(encoder.encode(rawPwd));
        return repo.save(u);
    }
    @Override public AppUser loadUserByUsername(String email) throws UsernameNotFoundException
    { return repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("not found"));
    }
}