package org.ssor.boss.transactions.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ssor.boss.entity.Transaction;
import org.ssor.boss.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class TransactionServiceTest
{

  @MockBean
  private TransactionRepository transactionRepository;
  @InjectMocks
  private TransactionService transactionService;

  private static Transaction stubbedTransaction;
  private static List<Transaction> stubbedTransactions;

  @BeforeAll
  static void setUp()
  {
    Transaction stubbedTransactionItemFromDao = new Transaction();

    stubbedTransactionItemFromDao.setAccountId(1);
    stubbedTransactionItemFromDao.setSucceeded(true);
    stubbedTransactionItemFromDao.setAtmTransactionId(1);
    stubbedTransactionItemFromDao.setPending(false);
    stubbedTransactionItemFromDao.setDate(LocalDateTime.now());
    stubbedTransactionItemFromDao.setOverdraftId(1);
    stubbedTransactionItemFromDao.setAmount(123.45f);
    stubbedTransactionItemFromDao.setId(1);
    stubbedTransactionItemFromDao.setMerchantName("Stubbed Merchant");
    stubbedTransactionItemFromDao.setNewBalance(12345.67f);

    stubbedTransaction = stubbedTransactionItemFromDao;
    stubbedTransactions = new ArrayList<>();
    stubbedTransactions.add(stubbedTransaction);

  }

  @Test
  void test_fetchTransactionsNoKeyword() throws NoTransactionFoundException
  {
    Mockito.doReturn(stubbedTransactions)
           .when(transactionRepository)
           .findTransactionsByAccountId(Mockito.anyInt(), Mockito.any());

    List<Transaction> actualTransactions = transactionService
        .fetchTransactions(Optional.empty(), Optional.of(0), Optional.of(1));

    assertEquals(stubbedTransactions, actualTransactions);
  }

  @Test
  void test_fetchTransactionsWithKeyword() throws NoTransactionFoundException
  {
    Mockito.doReturn(stubbedTransactions)
           .when(transactionRepository)
           .findTransactionsByAccountIdLikeMerchantName(Mockito.anyInt(), Mockito.any(), Mockito.any());

    List<Transaction> actualTransactions = transactionService
        .fetchTransactions(Optional.of("KeyTest"), Optional.of(0), Optional.of(1));

    assertEquals(stubbedTransactions, actualTransactions);
  }

  @Test
  void test_canFetchAccountTransactionById() throws NoTransactionFoundException
  {

    Mockito.doReturn(stubbedTransaction)
           .when(transactionRepository)
           .findTransactionById(Mockito.anyInt(), Mockito.anyInt());

    Transaction actual = transactionService
        .fetchAccountTransactionById(Optional.of(1), Optional.of(1));

    assertEquals(stubbedTransaction, actual);

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
        transactionService.fetchTransactions(Optional.of("TestKeyword"), Optional.of(0), Optional.of(-1))
    );

    String expectedMessage = "No Transaction Found";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

}
