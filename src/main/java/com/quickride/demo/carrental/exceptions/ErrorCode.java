package com.quickride.demo.carrental.exceptions;

public enum ErrorCode {
    CAR_NOT_FOUND("Car not found"),
    RESERVATION_NOT_FOUND("Reservation not found"),
    USER_NOT_FOUND("User not found"),
    WRONG_LOGIN_OR_PASSWORD("Wrong login or password"),
    CAR_ALREADY_BOOKED("This car is already booked for the selected dates.");

    ErrorCode(String message) {
        this.message = message;
    }

    final String message;
}