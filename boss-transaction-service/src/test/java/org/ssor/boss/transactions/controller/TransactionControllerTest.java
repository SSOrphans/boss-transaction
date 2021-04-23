package org.ssor.boss.transactions.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ssor.boss.entity.Transaction;
import org.ssor.boss.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.service.TransactionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class TransactionControllerTest
{
  @Mock
  TransactionService transactionService;
  @InjectMocks
  TransactionController transactionController;

  private static Transaction stubbedTransaction;
  private static List<Transaction> stubbedTransactions;

  @BeforeAll
  static void setUp()
  {
    Transaction transaction = new Transaction();

    transaction.setSucceeded(true);
    transaction.setAtmTransactionId(1);
    transaction.setPending(false);
    transaction.setDate(LocalDateTime.now());
    transaction.setOverdraftId(1);
    transaction.setAmount(123.45f);
    transaction.setId(1);
    transaction.setMerchantName("Stubbed Merchant");
    transaction.setNewBalance(12345.67f);

    stubbedTransaction = transaction;
    stubbedTransactions = new ArrayList<>();
    stubbedTransactions.add(stubbedTransaction);
  }

  @Test
  void test_canGetTransactionsWithoutKeyword() throws NoTransactionFoundException
  {
    Mockito.doReturn(stubbedTransactions)
           .when(transactionService)
           .fetchTransactions(Mockito.any(), Mockito.any(), Mockito.any());

    assertEquals(stubbedTransactions,
                 transactionController
                     .getTransactions(Optional.ofNullable(null), null, Optional.of(1)));
  }

  @Test
  void test_canGetTransaction() throws NoTransactionFoundException
  {
    Mockito.doReturn(stubbedTransaction)
           .when(transactionService)
           .fetchAccountTransactionById(Mockito.any(), Mockito.any());

    assertEquals(stubbedTransaction,
                 transactionController
                     .getTransaction(Optional.of(1), Optional.of(1)));
  }

  @Test
  void test_willThrowExceptionOnBadRouteGetTransactions() throws NoTransactionFoundException
  {
    NoTransactionFoundException ntfe = new NoTransactionFoundException();
    Mockito.doThrow(ntfe).when(transactionService).fetchTransactions(Mockito.any(), Mockito.any(), Mockito.any());
    Exception exception = assertThrows(NoTransactionFoundException.class, () ->
        transactionController.getTransactions(Optional.ofNullable(null), null, Optional.of(1))
    );

    String expectedMessage = "No Transaction Found";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void test_willThrowExceptionOnBadRouteGetTransaction() throws NoTransactionFoundException
  {
    NoTransactionFoundException ntfe = new NoTransactionFoundException();
    Mockito.doThrow(ntfe).when(transactionService).fetchAccountTransactionById(Mockito.any(), Mockito.any());
    Exception exception = assertThrows(NoTransactionFoundException.class, () ->
        transactionController.getTransaction(Optional.of(1),Optional.of(1))
    );

    String expectedMessage = "No Transaction Found";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

}
