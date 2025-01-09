package com.quickride.demo.carrental.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Car {

    @Id
    private String id;

    private String make;
    private String model;
    private Integer year;
    private BigDecimal pricePerDay;

    @Enumerated(EnumType.STRING)
    private CarCategory category;

    private boolean available;

}
