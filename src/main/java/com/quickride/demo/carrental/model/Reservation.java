package com.quickride.demo.carrental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Reservation {

    @Id
    private String id;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Car car;

    private boolean confirmed;
}
