package com.quickride.demo.carrental.forms;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationForm {

    @NotNull(message = "Start date must not be null")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date must not be null")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotBlank(message = "AppUser ID must not be blank")
    @Size(min = 36, max = 36, message = "AppUser ID must have 36 characters.")
    private String appUserId;

    @NotBlank(message = "Car ID must not be blank")
    @Size(min = 36, max = 36, message = "Car ID must have 36 characters.")
    private String carId;

    @NotNull(message = "Full price must not be null")
    @DecimalMin(value = "0.01", message = "Full price must be at least 0.01.")
    private BigDecimal fullPrice;
}
