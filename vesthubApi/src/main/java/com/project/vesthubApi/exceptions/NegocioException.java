package com.project.vesthubApi.exceptions;

public class NegocioException extends RuntimeException{
    public NegocioException(String message) {
        super(message);
    }
}
