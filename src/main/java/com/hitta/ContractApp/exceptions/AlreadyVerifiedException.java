package com.hitta.ContractApp.exceptions;

public class AlreadyVerifiedException extends RuntimeException {
    public AlreadyVerifiedException(String message) {
        super(message);
    }
}
