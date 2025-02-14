package com.quickride.demo.carrental.service;

import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.exceptions.ErrorCode;
import com.quickride.demo.carrental.forms.EditUserForm;
import com.quickride.demo.carrental.forms.LoginForm;
import com.quickride.demo.carrental.forms.RegisterForm;
import com.quickride.demo.carrental.model.AppUser;
import com.quickride.demo.carrental.model.Role;
import com.quickride.demo.carrental.repository.UserRepository;
import com.quickride.demo.carrental.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String login(@Valid LoginForm form) throws ApplicationException {
        AppUser user = userRepository.findByEmail(form.getEmail()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        if (passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            return jwtTokenProvider.generateToken(user.getId(), user.getRole());
        }
        throw new ApplicationException(ErrorCode.WRONG_LOGIN_OR_PASSWORD);
    }

    public void registerUser(@Valid RegisterForm registerForm) {
        AppUser appUser = AppUser.builder()
                .id(UUID.randomUUID().toString())
                .firstName(registerForm.getFirstName())
                .lastName(registerForm.getLastName())
                .password(passwordEncoder.encode(registerForm.getPassword()))
                .email(registerForm.getEmail())
                .role(Role.USER)
                .build();
        userRepository.save(appUser);
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

    public AppUser editUser(String id, @Valid EditUserForm editUserForm) {
        AppUser user = userRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        user.setFirstName(editUserForm.getFirstName());
        user.setLastName(editUserForm.getLastName());
        user.setEmail(editUserForm.getEmail());
        return userRepository.save(user);
    }
}