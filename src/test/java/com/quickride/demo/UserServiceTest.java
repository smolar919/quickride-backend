package com.quickride.demo;

import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.forms.EditUserForm;
import com.quickride.demo.carrental.forms.LoginForm;
import com.quickride.demo.carrental.forms.RegisterForm;
import com.quickride.demo.carrental.model.AppUser;
import com.quickride.demo.carrental.model.Role;
import com.quickride.demo.carrental.repository.UserRepository;
import com.quickride.demo.carrental.security.JwtTokenProvider;
import com.quickride.demo.carrental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        AppUser user = new AppUser();
        user.setId("user123");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("test@example.com");
        loginForm.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken("user123", Role.USER)).thenReturn("mockedToken");

        String token = userService.login(loginForm);

        assertNotNull(token);
        assertEquals("mockedToken", token);
    }

    @Test
    void testLogin_UserNotFound() {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("unknown@example.com");
        loginForm.setPassword("password");

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> userService.login(loginForm));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLogin_WrongPassword() {
        AppUser user = new AppUser();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("test@example.com");
        loginForm.setPassword("wrongPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> userService.login(loginForm));
        assertEquals("Wrong login or password", exception.getMessage());
    }

    @Test
    void testRegisterUser() {
        RegisterForm registerForm = new RegisterForm();
        registerForm.setFirstName("John");
        registerForm.setLastName("Doe");
        registerForm.setEmail("john@example.com");
        registerForm.setPassword("password123");

        AppUser newUser = AppUser.builder()
                .id(UUID.randomUUID().toString())
                .firstName(registerForm.getFirstName())
                .lastName(registerForm.getLastName())
                .password("encodedPassword")
                .email(registerForm.getEmail())
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(AppUser.class))).thenReturn(newUser);

        userService.registerUser(registerForm);

        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void testFindByEmail_Success() {
        AppUser user = new AppUser();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        AppUser foundUser = userService.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> userService.findByEmail("unknown@example.com"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testFindById_Success() {
        AppUser user = new AppUser();
        user.setId("user123");

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        AppUser foundUser = userService.findById("user123");

        assertNotNull(foundUser);
        assertEquals("user123", foundUser.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> userService.findById("user123"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testCreateSysAdminOnStartup_AlreadyExists() {
        when(userRepository.findByEmail("admin@test.pl")).thenReturn(Optional.of(new AppUser()));

        userService.createSysAdminOnStartup();

        verify(userRepository, never()).save(any(AppUser.class));
    }

    @Test
    void testCreateSysAdminOnStartup_CreatesAdmin() {
        when(userRepository.findByEmail("admin@test.pl")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("admin")).thenReturn("encodedPassword");

        userService.createSysAdminOnStartup();

        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void testEditUser_Success() {
        AppUser existingUser = new AppUser();
        existingUser.setId("user123");
        existingUser.setFirstName("OldName");
        existingUser.setLastName("OldLast");
        existingUser.setEmail("old@example.com");

        EditUserForm editUserForm = new EditUserForm();
        editUserForm.setFirstName("NewName");
        editUserForm.setLastName("NewLast");
        editUserForm.setEmail("new@example.com");

        when(userRepository.findById("user123")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(AppUser.class))).thenReturn(existingUser);

        AppUser updatedUser = userService.editUser("user123", editUserForm);

        assertNotNull(updatedUser);
        assertEquals("NewName", updatedUser.getFirstName());
        assertEquals("NewLast", updatedUser.getLastName());
        assertEquals("new@example.com", updatedUser.getEmail());
    }

    @Test
    void testEditUser_NotFound() {
        EditUserForm editUserForm = new EditUserForm();
        editUserForm.setFirstName("NewName");

        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> userService.editUser("user123", editUserForm));
        assertEquals("User not found", exception.getMessage());
    }
}
