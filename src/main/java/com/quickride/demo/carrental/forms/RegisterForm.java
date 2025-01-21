package com.quickride.demo.carrental.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterForm {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
