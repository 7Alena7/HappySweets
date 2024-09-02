package com.alena.happysweets.exceptions;
//Category deletion Exception will be thrown when admin tries to delete the category that is not empty
public class CategoryDeletionException extends RuntimeException{
    /*This constructor allows
    to specify a custom error message when the exception is thrown.*/
    public CategoryDeletionException(String message){
        super(message);
    }
    /* This constructor allows you to specify both
    a custom error message and a cause
    (another exception that led to this exception being thrown).*/
    public CategoryDeletionException(String message, Throwable cause){
        super(message, cause);
    }

}
