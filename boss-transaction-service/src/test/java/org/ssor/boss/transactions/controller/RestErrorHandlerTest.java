package org.ssor.boss.transactions.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ssor.boss.core.exception.BadRouteException;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.transfer.ErrorMessage;

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
    ErrorMessage response = restErrorHandler.processRouteError();
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    assertEquals(BadRouteException.MESSAGE, response.getMessage());
  }

  @Test
  void test_canReturnResponseEntityBadRouteExceptionOnMethod(){
    ErrorMessage responseGet = restErrorHandler.processGetRouteError();
    ErrorMessage responsePost = restErrorHandler.processPostRouteError();
    ErrorMessage responseDelete = restErrorHandler.processDeleteRouteError();

    ErrorMessage errorMessage = restErrorHandler.processRouteError();
    assertEquals(errorMessage, responsePost);
    assertEquals(errorMessage, responseDelete);
    assertEquals(errorMessage, responseGet);
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
    ErrorMessage response = restErrorHandler.processCatchUnhandledException();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
    assertEquals("Internal Error: Contact the administrator for help", response.getMessage());
  }

  @Test
  void test_canReturnGenericExceptionOnMethod(){
    ErrorMessage responseGet = restErrorHandler.processGetCatchUnhandledException();
    ErrorMessage responsePost = restErrorHandler.processPostCatchUnhandledException();
    ErrorMessage responseDelete = restErrorHandler.processDeleteCatchUnhandledException();

    ErrorMessage errorMessage = restErrorHandler.processCatchUnhandledException();
    assertEquals(errorMessage, responsePost);
    assertEquals(errorMessage, responseDelete);
    assertEquals(errorMessage, responseGet);
  }
}
