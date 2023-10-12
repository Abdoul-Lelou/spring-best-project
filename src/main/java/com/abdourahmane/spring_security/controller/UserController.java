package com.abdourahmane.spring_security.controller;

import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer.JwtConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdourahmane.spring_security.config.JwtService;
import com.abdourahmane.spring_security.dto.AuthenticationRequest;
// import com.abdourahmane.spring_security.config.JwtService;
// import com.abdourahmane.spring_security.dto.AuthenticationRequest;
import com.abdourahmane.spring_security.entity.User;
import com.abdourahmane.spring_security.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // @Autowired
    // public UserController(UserService userService) {
    //     this.userService = userService;
    // }

    @GetMapping(path = "/")
    public ResponseEntity<List<User>> listUsers() {
        List<User> userList = userService.getUsers().getBody();
        return ResponseEntity.ok(userList);
    }

    @PostMapping(path = "/")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User createUser = userService.saveUser(user); 
        return ResponseEntity.ok(createUser);
    }

    @PostMapping(path = "/users")
    public List<User> addUsers(@RequestBody User user) {
        System.out.println("okkkkkkkkkkkkkkkk");
        User createUser = userService.saveUser(user); 
        return List.of(createUser);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest authenticationRequest)  {
        var jwt= "";
        try {
            final Authentication aManager = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.email, authenticationRequest.password)
            );

            if (aManager.isAuthenticated()) {
                    // String generatedToken = jwtService.generateToken(authenticationRequest);
                     jwt = jwtService.generateToken(authenticationRequest);
                    System.out.print(jwt);
                    
            }
        

            return ResponseEntity.ok(jwt);

        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
   
}