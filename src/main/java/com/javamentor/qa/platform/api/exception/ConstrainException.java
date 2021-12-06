package com.javamentor.qa.platform.api.exception;

public class ConstrainException extends RuntimeException {
    private static final long serialVersionUID = 5723952907135446546L;

    public ConstrainException() {
    }

    public ConstrainException(String message) {
        super(message);
    }
}
