package com.quickride.demo.carrental.service;

import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.exceptions.ErrorCode;
import com.quickride.demo.carrental.forms.LoginForm;
import com.quickride.demo.carrental.model.AppUser;
import com.quickride.demo.carrental.model.Role;
import com.quickride.demo.carrental.repository.UserRepository;
import com.quickride.demo.carrental.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String login(LoginForm form) throws ApplicationException {
        AppUser user = userRepository.findByEmail(form.getEmail()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        if (passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            return jwtTokenProvider.generateToken(user.getId());
        }
        throw new ApplicationException(ErrorCode.WRONG_LOGIN_OR_PASSWORD);
    }

    public AppUser registerUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        userRepository.save(appUser);
        return appUser;
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    public AppUser findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    @EventListener(ContextRefreshedEvent.class)
    public void createSysAdminOnStartup() {

        if (userRepository.findByEmail("admin@test.pl").isPresent()) {
            return;
        }

        AppUser userAuth = AppUser.builder()
                .id(UUID.randomUUID().toString())
                .firstName("Admin")
                .lastName("Admin")
                .password(passwordEncoder.encode("admin"))
                .email("admin@test.pl")
                .role(Role.ADMIN)
                .build();

        userRepository.save(userAuth);
    }
}