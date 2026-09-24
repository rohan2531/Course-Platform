package com.project.Course.Platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.Course.Platform.entity.Role;
import com.project.Course.Platform.entity.User;
import com.project.Course.Platform.repository.UserRepository;

@Service 
public class UserService {
    private final UserRepository repo;
    @Autowired 
    private AuthenticationManager authenticationManager;
    @Autowired 
    private PasswordEncoder passwordEncoder;
    @Autowired 
    private JwtService jwt;

    UserService(UserRepository repo) {
        this.repo = repo;
    }
    
    public User saveUser(User user) 
    {
        user.setRole(Role.STUDENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }
    public String logInUser(User user) 
    {
        UsernamePasswordAuthenticationToken token =new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        try {
            authenticationManager.authenticate(token);
            User dbUser = repo.findByEmail(user.getEmail());
            return jwt.generateToken(user.getEmail(), dbUser.getRole());
            } catch (AuthenticationException e) {
                return "Wrong Credentials";
        }
        
    }

}
