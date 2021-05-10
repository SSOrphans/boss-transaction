package org.ssor.boss.transactions.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ssor.boss.core.entity.Transaction;
import org.ssor.boss.core.entity.TransactionType;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.service.TransactionOptions;
import org.ssor.boss.transactions.service.TransactionService;
import org.ssor.boss.transactions.transfer.TransactionListTransfer;
import org.ssor.boss.transactions.transfer.TransactionTransfer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    Transaction transactionA = new Transaction();

    transactionA.setSucceeded(true);
    transactionA.setAtmTransactionId(1);
    transactionA.setPending(false);
    transactionA.setDate(LocalDateTime.now());
    transactionA.setOverdraftId(1);
    transactionA.setAmount(123.45f);
    transactionA.setId(1);
    transactionA.setMerchantName("Stubbed Merchant");
    transactionA.setNewBalance(12345.67f);
    transactionA.setType(TransactionType.TRANSACTION_DEPOSIT);

    Transaction transactionB = new Transaction();

    transactionB.setSucceeded(true);
    transactionB.setAtmTransactionId(1);
    transactionB.setPending(false);
    transactionB.setDate(LocalDateTime.now());
    transactionB.setOverdraftId(1);
    transactionB.setAmount(123.45f);
    transactionB.setId(1);
    transactionB.setMerchantName("Stubbed Merchant");
    transactionB.setNewBalance(12345.67f);
    transactionB.setType(TransactionType.TRANSACTION_PAYMENT);

    stubbedTransaction = transactionA;
    stubbedTransactions = new ArrayList<>();
    stubbedTransactions.add(stubbedTransaction);
  }

  @Test
  void test_canGetTransactionsWithoutKeyword() throws NoTransactionFoundException
  {
    List<TransactionTransfer> transactionTransfers =
        stubbedTransactions.stream().map(TransactionTransfer::new).collect(Collectors.toList());
    TransactionListTransfer transfer = new TransactionListTransfer(transactionTransfers,1,1,1);
    Mockito.doReturn(transfer)
           .when(transactionService)
           .fetchTransactions(Mockito.any(TransactionOptions.class), Mockito.any());

    assertEquals(transfer,
                 transactionController
                     .getTransactions(
                         Optional.ofNullable(null),
                         Optional.ofNullable(null),
                         Optional.ofNullable(null),
                         Optional.ofNullable(null),
                         Optional.ofNullable(null),
                         Optional.ofNullable(null),
                         Optional.of(1)
                     )
    );
  }

  @Test
  void test_canGetTransaction() throws NoTransactionFoundException
  {
    Mockito.doReturn(new TransactionTransfer(stubbedTransaction))
           .when(transactionService)
           .fetchAccountTransactionById(Mockito.any(), Mockito.any());

    assertEquals(new TransactionTransfer(stubbedTransaction),
                 transactionController
                     .getTransaction(Optional.of(1), Optional.of(1)));
  }

  @Test
  void test_willThrowExceptionOnBadRouteGetTransactions() throws NoTransactionFoundException
  {
    NoTransactionFoundException ntfe = new NoTransactionFoundException();
    Mockito.doThrow(ntfe).when(transactionService).fetchTransactions(Mockito.any(TransactionOptions.class), Mockito.any());
    Exception exception = assertThrows(NoTransactionFoundException.class, () ->
        transactionController.getTransactions(
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.ofNullable(null),
            Optional.of(1)
        )
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
