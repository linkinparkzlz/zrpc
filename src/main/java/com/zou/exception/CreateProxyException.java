package com.zou.exception;

public class CreateProxyException extends RuntimeException {

    public CreateProxyException() {
        super();
    }

    public CreateProxyException(String message) {
        super(message);
    }


    public CreateProxyException(Throwable cause) {
        super(cause);
    }
}
