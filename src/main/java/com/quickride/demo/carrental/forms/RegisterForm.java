package com.quickride.demo.carrental.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterForm {

    @NotBlank(message = "First name must not be blank")
    @Length(min = 2, message = "First name must be at least 2 characters long")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Length(min = 2, message = "Last name must be at least 2 characters long")
    private String lastName;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is invalid")
    private String email;
}
