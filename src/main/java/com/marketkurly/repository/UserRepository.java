package com.marketkurly.repository;

import com.marketkurly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String Email);
    Optional<User> findByPassword(String password);
    Optional<User> findByUsername(String username);
}
