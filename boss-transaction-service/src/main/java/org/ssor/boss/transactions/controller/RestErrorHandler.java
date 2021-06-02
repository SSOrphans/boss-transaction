package org.ssor.boss.transactions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.transfer.ErrorMessage;

@RestControllerAdvice
public class RestErrorHandler
{

  @ExceptionHandler(NoTransactionFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @GetMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public ErrorMessage processNoTransactionFoundOccurred()
  {
    return new ErrorMessage(HttpStatus.NOT_FOUND, NoTransactionFoundException.MESSAGE);
  }

  @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
  @GetMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ErrorMessage processCatchIndexOutOfBounds()
  {
    return new ErrorMessage(HttpStatus.NOT_FOUND, "You have tried to access information that does not exist.");
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorMessage> processCatchUnhandledException()
  {
    var message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error: Contact the administrator for help");
    return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
