package com.quickride.demo.carrental.repository;

import com.quickride.demo.carrental.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, String> {
    Optional<AppUser> findByEmail(String email);
}
