package com.alena.happysweets.exceptions;
//ResourceNotFoundException will be thrown when user tries to reach something that doesn't exist
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
    public ResourceNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
