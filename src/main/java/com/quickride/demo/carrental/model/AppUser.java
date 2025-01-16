package com.quickride.demo.carrental.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "application_user")
public class AppUser {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;
}
