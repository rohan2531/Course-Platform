package com.project.Course.Platform.component;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.Course.Platform.entity.Role;
import com.project.Course.Platform.service.CustomUserDetailsService;
import com.project.Course.Platform.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class JwtFilter extends OncePerRequestFilter{
    @Autowired 
    private JwtService jwtService;
    @Autowired
    CustomUserDetailsService customUserDetailsService; 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException 
            {
                String authHeader=request.getHeader("Authorization");
                String email=null;
                String token =null;
                if(authHeader!=null && authHeader.startsWith("Bearer "))
                {
                    token=authHeader.substring(7);
                    email=jwtService.extractUserName(token);
                }
                if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null)
                {
                    UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(email);
                    if (jwtService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                        System.out.println(userDetails.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        System.out.println(
    "AUTH = " +
    SecurityContextHolder.getContext().getAuthentication()
);
                    }
                
                }
                filterChain.doFilter(request, response);
            }

}
