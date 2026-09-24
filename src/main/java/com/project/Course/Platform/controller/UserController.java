package com.project.Course.Platform.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.Course.Platform.entity.Role;
import com.project.Course.Platform.entity.User;
import com.project.Course.Platform.service.JwtService;
import com.project.Course.Platform.service.UserService;

import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired 
    private UserService userService;
    @Autowired 
    private JwtService jwt;

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        
        userService.saveUser(user);
        return "User Registered !!";        
    }
   
    @PostMapping("/login")
     public ResponseEntity<String>login(@RequestBody User user) 
    {

         String s=userService.logInUser(user);
         if(!"Wrong Credentials".equals(s))
         {
            return ResponseEntity.ok(s);
         }
         else
         {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong Credentials");
         }
    }
    @GetMapping("/path")
    public String getMethodName()  {
            
            return "working";
            
    }
    
    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String test() {
       return "Admin Testing successful!!";
    }
    @GetMapping("/manage-test")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public String manageTest() {
        return "Management access granted";
        }
    @GetMapping("/admin/admin-test")
        public String adminTest() {
            return "Admin test";
            }
    
    
    
    
}
