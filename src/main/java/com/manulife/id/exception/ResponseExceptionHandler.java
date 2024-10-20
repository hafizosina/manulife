package com.manulife.id.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(ProcessException.class)
    @ResponseBody
    public ResponseEntity<Object> handleProcessException(ProcessException ex) {
        ErrorResponse errorMessage = new ErrorResponse(ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<Object> handleProcessException(BadRequestException ex) {
        ErrorResponse errorMessage = new ErrorResponse(ex);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
