package com.quickride.demo.carrental.forms;

import com.quickride.demo.carrental.model.CarCategory;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCarForm {

    @NotBlank(message = "Make must not be blank")
    @Size(max = 50, message = "Make must have at most 50 characters.")
    private String make;

    @NotBlank(message = "Model must not be blank")
    @Size(max = 50, message = "Model must have at most 50 characters.")
    private String model;

    @NotNull(message = "Year must not be null")
    @Min(value = 1886, message = "Year must be at least 1886 (first car invention year).")
    @Max(value = 2100, message = "Year must not be in the far future.")
    private Integer year;

    @NotNull(message = "Price per day must not be null")
    @DecimalMin(value = "0.01", message = "Price per day must be at least 0.01.")
    private BigDecimal pricePerDay;

    @NotNull(message = "Category must not be null")
    private CarCategory category;
}
