package org.ssor.boss.transactions.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class TransactionHealthControllerTest
{
  @InjectMocks
  TransactionHealthController transactionController;

  @Test
  void test_canGetHealthyStatus(){
    var responseEntity = transactionController.transactionHealth();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals("Healthy", responseEntity.getBody());
  }
}
