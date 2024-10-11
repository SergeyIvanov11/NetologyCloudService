package com.netology.netologycloudservice.exception;

public class IncorrectFileFormatException extends RuntimeException{

    public IncorrectFileFormatException() {
    }

    public IncorrectFileFormatException(String message) {
        super(message);
    }

    public IncorrectFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFileFormatException(Throwable cause) {
        super(cause);
    }
}
