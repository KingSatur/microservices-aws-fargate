package com.jdcam.microservices.users.exception;

public class AlreadyExistsEmailException extends Exception{

    public AlreadyExistsEmailException(String message){
        super(message);
    }
}
