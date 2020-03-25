package com.lykke.api.testing.exceptions;

public class ResponseValidationException extends Exception {

    public ResponseValidationException(String errorMessage) {
        super(errorMessage);
    }
}
