package com.easybustickets.ticket_buying_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException exception) {
        return exception.getLocalizedMessage();
    }

    @ExceptionHandler(TicketAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTicketAmountException(TicketAmountException exception) {
        return exception.getLocalizedMessage();
    }
}
