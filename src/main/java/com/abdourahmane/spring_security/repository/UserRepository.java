package com.abdourahmane.spring_security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdourahmane.spring_security.entity.User;

public interface UserRepository extends JpaRepository<User, Long>  {
    Optional<User>findByUsername(String username);
}
