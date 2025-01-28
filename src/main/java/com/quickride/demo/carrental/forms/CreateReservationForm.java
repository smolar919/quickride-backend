package com.quickride.demo.carrental.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationForm {
    private LocalDate startDate;
    private LocalDate endDate;
    private String appUserId;
    private String carId;
    private BigDecimal fullPrice;
}
