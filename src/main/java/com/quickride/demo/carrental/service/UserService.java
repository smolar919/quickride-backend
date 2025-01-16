package com.quickride.demo.carrental.service;

import com.quickride.demo.carrental.model.AppUser;
import com.quickride.demo.carrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AppUser registerUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        userRepository.save(appUser);
        return appUser;
    }

    public AppUser findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}