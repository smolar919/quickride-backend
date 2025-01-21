package com.quickride.demo.carrental.forms;

import com.quickride.demo.carrental.model.CarCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditCarForm {
    private String make;
    private String model;
    private Integer year;
    private BigDecimal pricePerDay;
    private CarCategory category;
}
