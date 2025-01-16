package com.quickride.demo.carrental.exceptions;

public enum ErrorCode {
    CAR_NOT_FOUND("Car not found"),
    RESERVATION_NOT_FOUND("Reservation not found"),
    USER_NOT_FOUND("User not found"),
    WRONG_LOGIN_OR_PASSWORD("Wrong login or password"),;

    ErrorCode(String message) {
        this.message = message;
    }

    final String message;
}