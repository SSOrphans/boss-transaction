package org.ssor.boss.transactions.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ssor.boss.core.exception.BadRouteException;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.transfer.ErrorMessage;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class RestErrorHandlerTest
{
  @InjectMocks
  RestErrorHandler restErrorHandler;

  @Test
  void test_canCreateRestErrorHandler()
  {
    RestErrorHandler reh = new RestErrorHandler();
    assertNotNull(reh);
  }

  @Test
  void test_canReturnResponseEntityBadRouteException()
  {
    ResponseEntity<ErrorMessage> response = restErrorHandler.processRouteError();

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(BadRouteException.MESSAGE, Objects.requireNonNull(response.getBody()).getMessage());
  }

  @Test
  void test_canReturnTransactionNotFoundException()
  {
    ErrorMessage response = restErrorHandler.processNoTransactionFoundOccurred();
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    assertEquals(NoTransactionFoundException.MESSAGE, response.getMessage());
  }

  @Test
  void test_canReturnIndexOutOfBoundsException()
  {
    ErrorMessage response = restErrorHandler.processCatchIndexOutOfBounds();
    assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    assertEquals("You have tried to access information that does not exist.", response.getMessage());
  }

  @Test
  void test_canReturnGenericException()
  {

    var response = restErrorHandler.processCatchUnhandledException();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Internal Error: Contact the administrator for help", Objects.requireNonNull(response.getBody()).getMessage());
  }

}
