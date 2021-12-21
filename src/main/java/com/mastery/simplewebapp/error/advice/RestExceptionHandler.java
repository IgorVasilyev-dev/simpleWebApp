package com.mastery.simplewebapp.error.advice;


import com.mastery.simplewebapp.error.ApiError;
import com.mastery.simplewebapp.error.exception.EntityNotFoundException;
import com.mastery.simplewebapp.error.exception.SQLConstraintException;
import com.mastery.simplewebapp.error.exception.SQLRuntimeException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Класс для перехвата Exception на контроллере и обработки их
 */
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    protected ResponseEntity<?> handlerEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        return ResponseEntity.badRequest().body(new ApiError(HttpStatus.NOT_FOUND,
                "Not found Entity", exception.getMessage()));
    }

    @ExceptionHandler(value = SQLRuntimeException.class)
    protected ResponseEntity<?> handlerSQLRuntimeException(SQLRuntimeException exception) {
        return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST,
                "Internal Server Error", exception.getMessage()));
    }

    @ExceptionHandler(value = SQLConstraintException.class)
    protected ResponseEntity<?> handlerSQLConstraintException(SQLConstraintException exception) {
        return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST,
                "Internal Server Error", exception.getMessage()));
    }


    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<?> handlerEntityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity.badRequest().body(new ApiError(HttpStatus.NOT_FOUND,
                "Not Found Entity", exception.getMessage()));
    }

}
