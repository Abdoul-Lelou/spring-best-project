package com.abdourahmane.spring_security.config;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.abdourahmane.spring_security.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;


public class JwtUtil {

    private final String secret_key = "mysecretkey";
    private long accessTokenValidity = 60*60*1000;

    private final JwtParser jwtParser;

   

    public JwtUtil(){
        this.jwtParser = (JwtParser) Jwts.parser().setSigningKey(secret_key);
    }

    public String createToken(User user) {
        Claims claims = (Claims) Jwts.claims().setSubject(user.getEmail());
        claims.put("username",user.getUsername());
        claims.put("lastName",user.getLastname());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (Exception e) {
            // logger.info("Error parsing JWT token: {}", e.getMessage());
            return null;
        }
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}