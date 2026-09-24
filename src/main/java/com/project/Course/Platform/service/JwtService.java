package com.project.Course.Platform.service;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.project.Course.Platform.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;


@Service 
public class JwtService {
    
    private  SecretKey secretKey;
    @Value ("${jwt.secret}")
    private String key;

    @PostConstruct 
    public void keyGenerationMethod()
    {
        byte[] keyBytes=Base64.getDecoder().decode(key);
       secretKey=new SecretKeySpec(keyBytes, "HmacSHA256");
    }
    


    public String generateToken(String email,Role role)
    {
        return Jwts.builder()
        .subject(email)
        .claim("role", role)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis()+3600*1000))
        .signWith(secretKey)
        .compact();

    }
    public String extractUserName(String token)
    {
        Claims claims=extractAllClaims(token);
        return claims.getSubject();
    }
    public Date extractExpiration(String token)
    {
        Claims claims=extractAllClaims(token);
        return claims.getExpiration();
    }
    public boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }
    private Claims extractAllClaims(String token)
    {
        return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }
    public  boolean validateToken(String Token,UserDetails userDetails)
    {
        String name=extractUserName(Token);
        return name.equals(userDetails.getUsername()) && !isTokenExpired(Token);
    }
    
}
