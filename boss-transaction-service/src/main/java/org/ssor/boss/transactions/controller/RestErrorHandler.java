package org.ssor.boss.transactions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.ssor.boss.core.exception.BadRouteException;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.transfer.ErrorMessage;

@RestControllerAdvice
public class RestErrorHandler
{
  @ExceptionHandler(BadRouteException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @RequestMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public ErrorMessage processRouteError()
  {
    return new ErrorMessage(HttpStatus.BAD_REQUEST, BadRouteException.MESSAGE);
  }

  @ExceptionHandler(NoTransactionFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @RequestMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public ErrorMessage processNoTransactionFoundOccurred()
  {
    return new ErrorMessage(HttpStatus.NOT_FOUND, NoTransactionFoundException.MESSAGE);
  }

  @ExceptionHandler(Exception.class)
  @RequestMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage processCatchUnhandledException()
  {
    return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error: Contact the administrator for help");
  }
}
