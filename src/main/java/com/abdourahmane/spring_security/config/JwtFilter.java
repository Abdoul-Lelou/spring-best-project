package com.abdourahmane.spring_security.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.abdourahmane.spring_security.entity.User;
import com.abdourahmane.spring_security.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
@Service
public class JwtFilter extends OncePerRequestFilter {
    
    private UserDetailsService userDetailsService;
    private JwtService jwtService;

    public JwtFilter(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("MY SERVICE:::::::" + jwtService);
        try {
            UserDetails username = null;
            String token = null;
            Boolean istokenValid= true;
            logger.info( "MON HEADER ++ " +request.getHeader("Authorization"));
            String autorizationHeader = request.getHeader("Authorization");
            if (autorizationHeader != null && autorizationHeader.startsWith("Bearer ")) {
                token = autorizationHeader.substring(7);
                var userToken = jwtService.extractUserName(token);
                username = this.userDetailsService.loadUserByUsername(userToken);
                // logger.info(username.getUsername()+ "OKKKK CA MARCHE ");
                istokenValid= jwtService.isTokenValid(token,   username);
                // logger.info(istokenValid+ "NONOO CA MARCHE PAS");
            }

            
            if (istokenValid && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails=  userDetailsService.loadUserByUsername(username.getUsername());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(autorizationHeader, userDetails.getUsername(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            filterChain.doFilter(request, response);
        } 
        catch (Exception e) {
            logger.info("THE ERROR MSG:::"+e.getMessage());
        }
    }
}
