package com.alena.happysweets.global;

import com.alena.happysweets.exceptions.CategoryDeletionException;
import com.alena.happysweets.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice //This annotation makes the class a global exception handler.
public class GlobalExceptionHandler {
    @ExceptionHandler(CategoryDeletionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)//sets the HTTP status code to 400 (Bad Request)
    public String handleCategoryDeletionException(CategoryDeletionException ex){
        return "category_deletion_error";
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException ex) {
        return "404";
    }



}
