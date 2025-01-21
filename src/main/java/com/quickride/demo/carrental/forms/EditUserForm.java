package com.quickride.demo.carrental.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserForm {
    private String firstName;
    private String lastName;
    private String email;
}
