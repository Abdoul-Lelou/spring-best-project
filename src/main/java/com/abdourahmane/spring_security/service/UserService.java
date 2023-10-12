package com.abdourahmane.spring_security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.abdourahmane.spring_security.entity.User;
import com.abdourahmane.spring_security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    // @Autowired 
    // public UserService(UserRepository userRepository){
        //     this.userRepository = userRepository;
        // }
        
        public ResponseEntity<List<User>> getUsers(){
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        }
        
        public User saveUser(User user){
            User addUser = userRepository.save(user);
            return addUser;
        }
        
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("MY DETAILS USERS:::::::"+ userRepository.findByUsername(username));
        
        User user = userRepository.findByUsername(username)
                                                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(roles.toArray(new String[0]))
                        .build();
        return userDetails;
    }
    //  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //      Objects.requireNonNull(username);
    //      User user = userRepository.findByEmail(username)
    //              .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    //      return user;
    //  }

    // public User saveUser(User user) {
    //     return userRepository.save(user);
    // }
}