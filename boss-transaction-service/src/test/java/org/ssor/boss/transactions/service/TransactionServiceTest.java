package org.ssor.boss.transactions.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ssor.boss.core.entity.Transaction;
import org.ssor.boss.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.repository.TransactionRepository;
import org.ssor.boss.transactions.transfer.TransactionTransfer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class TransactionServiceTest
{

  @MockBean
  private TransactionRepository transactionRepository;
  @InjectMocks
  private TransactionService transactionService;

  private static Transaction stubbedTransactionA;
  private static Transaction stubbedTransactionB;
  private static List<Transaction> stubbedTransactions;

  @BeforeAll
  static void setUp()
  {
    Transaction transactionA = new Transaction();

    transactionA.setAccountId(1);
    transactionA.setSucceeded(true);
    transactionA.setAtmTransactionId(1);
    transactionA.setPending(false);
    transactionA.setDate(LocalDateTime.now());
    transactionA.setOverdraftId(1);
    transactionA.setAmount(123.45f);
    transactionA.setId(1);
    transactionA.setMerchantName("Stubbed Merchant");
    transactionA.setNewBalance(12345.67f);

    Transaction transactionB = new Transaction();

    transactionB.setAccountId(1);
    transactionB.setSucceeded(true);
    transactionB.setAtmTransactionId(1);
    transactionB.setPending(false);
    transactionB.setDate(LocalDateTime.now().minusDays(5));
    transactionB.setOverdraftId(1);
    transactionB.setAmount(123.45f);
    transactionB.setId(1);
    transactionB.setMerchantName("Stubbed Another");
    transactionB.setNewBalance(12345.67f);

    stubbedTransactionA = transactionA;
    stubbedTransactionB = transactionB;
    stubbedTransactions = new ArrayList<>();
    stubbedTransactions.add(TransactionServiceTest.stubbedTransactionA);
    stubbedTransactions.add(TransactionServiceTest.stubbedTransactionB);

  }

  @Test
  void test_fetchTransactionsNoKeyword() throws NoTransactionFoundException
  {
    Mockito.doReturn(stubbedTransactions)
           .when(transactionRepository)
           .findTransactionsByAccountId(Mockito.anyInt(), Mockito.any());

    List<TransactionTransfer> actualTransactions = transactionService
        .fetchTransactions(Optional.empty(), Optional.of(0), Optional.of(10), Optional.of(1));

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    stubbedTransactions.forEach(t-> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions);
  }

  @Test
  void test_fetchTransactionsWithKeyword() throws NoTransactionFoundException
  {
    Mockito.doReturn(stubbedTransactions)
           .when(transactionRepository)
           .findTransactionsByAccountIdLikeMerchantName(Mockito.anyInt(), Mockito.any(), Mockito.any());

    List<TransactionTransfer> actualTransactions = transactionService
        .fetchTransactions(Optional.of("KeyTest"), Optional.of(0), Optional.of(10), Optional.of(1));

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    stubbedTransactions.forEach(t-> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions);
  }

  @Test
  void test_canLimitTransactions() throws NoTransactionFoundException
  {
    int limit = 1;
    List<Transaction> limitedTransactions = stubbedTransactions.subList(0, limit);

    Mockito.doReturn(limitedTransactions)
           .when(transactionRepository)
           .findTransactionsByAccountIdLikeMerchantName(Mockito.anyInt(), Mockito.any(), Mockito.any());

    List<TransactionTransfer> actualTransactions = transactionService
        .fetchTransactions(Optional.of("KeyTest"), Optional.of(0), Optional.of(limit), Optional.of(1));

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    limitedTransactions.forEach(t-> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions);
  }

  @Test
  void test_canPageTransactions() throws NoTransactionFoundException
  {
    int limit = 1;
    int page = 1;
    List<Transaction> pagedTransaction = stubbedTransactions.subList(1, 2);

    Mockito.doReturn(pagedTransaction)
           .when(transactionRepository)
           .findTransactionsByAccountIdLikeMerchantName(Mockito.anyInt(), Mockito.any(), Mockito.any());

    List<TransactionTransfer> actualTransactions = transactionService
        .fetchTransactions(Optional.of("KeyTest"), Optional.of(page), Optional.of(limit), Optional.of(1));

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    pagedTransaction.forEach(t-> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions);
  }

  @Test
  void test_canFetchAccountTransactionById() throws NoTransactionFoundException
  {

    Mockito.doReturn(stubbedTransactionA)
           .when(transactionRepository)
           .findTransactionById(Mockito.anyInt(), Mockito.anyInt());

    TransactionTransfer expected = new TransactionTransfer(stubbedTransactionA);
    TransactionTransfer actual = transactionService.fetchAccountTransactionById(Optional.of(1), Optional.of(1));

    assertEquals(expected, actual);

  }

  @Test
  void test_willThrowExceptionOnBadAccountIdFetchAccountTransactionById()
  {
    Exception exception = assertThrows(NoTransactionFoundException.class, () ->
        transactionService.fetchAccountTransactionById(Optional.of(1), Optional.of(-1))
    );

    String expectedMessage = "No Transaction Found";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void test_willThrowExceptionOnBadAccountIdFetchTransactions()
  {
    Exception exception = assertThrows(NoTransactionFoundException.class, () ->
        transactionService
            .fetchTransactions(Optional.of("TestKeyword"), Optional.of(0), Optional.of(10), Optional.of(-1))
    );

    String expectedMessage = "No Transaction Found";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

}
