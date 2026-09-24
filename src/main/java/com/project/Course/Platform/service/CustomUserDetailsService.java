package com.project.Course.Platform.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.Course.Platform.entity.User;
import com.project.Course.Platform.repository.UserRepository;

@Service 
public class CustomUserDetailsService implements UserDetailsService{
    final UserRepository repo;

    CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =repo.findByEmail(username);
        if(user==null)
        {
            throw new UsernameNotFoundException("User not exist");
        }
        return  org.springframework.security.core.userdetails.User
        .withUsername(user.getEmail())
        .roles(user.getRole().name())
        .password(user.getPassword())
        .build();
        
    }

}
