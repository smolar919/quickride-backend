package com.quickride.demo.carrental.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditReservationForm {
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean confirmed;
}
