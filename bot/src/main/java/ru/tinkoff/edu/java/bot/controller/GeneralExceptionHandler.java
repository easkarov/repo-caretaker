package ru.tinkoff.edu.java.bot.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.tinkoff.edu.java.bot.dto.response.ApiErrorResponse;

@Component
@RestControllerAdvice
public class GeneralExceptionHandler {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ApiErrorResponse handleMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ApiErrorResponse.builder()
                .description("Missing or invalid request value")
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .exceptionName(exception.getClass().getSimpleName())
                .exceptionMessage(exception.getMessage())
                .build();
    }


    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ApiErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return ApiErrorResponse.builder()
                .description("Type mismatch with (%s) parameter".formatted(exception.getName()))
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .exceptionName(exception.getClass().getSimpleName())
                .exceptionMessage(exception.getMessage())
                .build();
    }
}
