package org.ssor.boss.transactions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.ssor.boss.core.exception.BadRouteException;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.transfer.ErrorMessage;

@RestControllerAdvice
public class RestErrorHandler
{
  @ExceptionHandler(BadRouteException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @GetMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public ErrorMessage processGetRouteError()
  {
    return processRouteError();
  }

  @ExceptionHandler(BadRouteException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @PostMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public ErrorMessage processPostRouteError()
  {
    return processRouteError();
  }

  @ExceptionHandler(BadRouteException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @DeleteMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public ErrorMessage processDeleteRouteError()
  {
    return processRouteError();
  }

  public ErrorMessage processRouteError()
  {
    return new ErrorMessage(HttpStatus.BAD_REQUEST, BadRouteException.MESSAGE);
  }

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
  @GetMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage processGetCatchUnhandledException()
  {
    return processCatchUnhandledException();
  }

  @ExceptionHandler(Exception.class)
  @PostMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage processPostCatchUnhandledException()
  {
    return processCatchUnhandledException();
  }

  @ExceptionHandler(Exception.class)
  @DeleteMapping(produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage processDeleteCatchUnhandledException()
  {
    return processCatchUnhandledException();
  }

  public ErrorMessage processCatchUnhandledException()
  {
    return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error: Contact the administrator for help");
  }
}
