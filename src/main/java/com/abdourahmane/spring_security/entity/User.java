package com.abdourahmane.spring_security.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Table(name = "user")
@Entity
@Data
public class User implements UserDetails {
    
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Nullable
    private Long id;

    @Column
    @NonNull
    private String firstname;

    @Column
    @NonNull
    private String lastname;

    @Column
    @NonNull
    private String username;

    @Column(unique = true)
    @NonNull
    private String email;

    
    @Column
    @NonNull
    private String password;
    
    public String getPassword() {
        return password;
    }

    // @Column(nullable = false)
    @Enumerated(EnumType.STRING)    
    private  Role role;

    public Role getRole() {
        return role;
    }

    public User(){}

    public User(String firstname, String lastname, String email, String username, String password, String role){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = Role.valueOf(role);
    }
    
    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
    
    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
       return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static Object withDefaultPasswordEncoder() {
        return null;
    }

}