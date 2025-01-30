package com.quickride.demo.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationPeriodDTO {
    private LocalDate startDate;
    private LocalDate endDate;
}
