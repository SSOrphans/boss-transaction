package org.ssor.boss.transactions.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ssor.boss.core.entity.Transaction;
import org.ssor.boss.core.entity.TransactionType;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.repository.TransactionRepository;
import org.ssor.boss.transactions.transfer.TransactionListTransfer;
import org.ssor.boss.transactions.transfer.TransactionTransfer;

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

  private static Transaction stubbedTransactionA;
  private static Transaction stubbedTransactionB;
  private static Page<Transaction> stubbedTransactions;

  private static Optional<Long> stubbedAccountId;
  private static Optional<Long> stubbedBadAccountId;

  @BeforeAll
  static void setUp()
  {
    stubbedAccountId = Optional.of(1L);
    stubbedBadAccountId = Optional.of(-1L);
    Transaction transactionA = new Transaction();

    transactionA.setAccountId(1L);
    transactionA.setSucceeded(true);
    transactionA.setAtmTransactionId(1);
    transactionA.setPending(false);
    transactionA.setDate(LocalDateTime.now());
    transactionA.setOverdraftId(1);
    transactionA.setAmount(123.45f);
    transactionA.setId(1);
    transactionA.setMerchantName("Stubbed Merchant");
    transactionA.setNewBalance(12345.67f);
    transactionA.setType(TransactionType.TRANSACTION_PAYMENT);

    Transaction transactionB = new Transaction();

    transactionB.setAccountId(1L);
    transactionB.setSucceeded(true);
    transactionB.setAtmTransactionId(1);
    transactionB.setPending(false);
    transactionB.setDate(LocalDateTime.now().minusDays(5));
    transactionB.setOverdraftId(1);
    transactionB.setAmount(123.45f);
    transactionB.setId(1);
    transactionB.setMerchantName("Stubbed Another");
    transactionB.setNewBalance(12345.67f);
    transactionB.setType(TransactionType.TRANSACTION_CHARGE);

    stubbedTransactionA = transactionA;
    stubbedTransactionB = transactionB;
    List<Transaction> transactions = new ArrayList<>();
    transactions.add(TransactionServiceTest.stubbedTransactionA);
    transactions.add(TransactionServiceTest.stubbedTransactionB);
    stubbedTransactions = new PageImpl<>(transactions);

  }

  @Test
  void test_fetchTransactionsNoKeyword() throws NoTransactionFoundException
  {
    Mockito.doReturn(stubbedTransactions)
           .when(transactionRepository)
           .findTransactionsByAccountIdWithOptions(Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.any());

    TransactionListTransfer actualTransactions = transactionService
        .fetchTransactions(
            new TransactionOptions("", "date", TransactionType.TRANSACTION_INVALID.toString(), "0", "10", "false"),
            stubbedAccountId);

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    stubbedTransactions.forEach(t -> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions.getTransactions());
  }

  @Test
  void test_fetchTransactionsWithKeyword() throws NoTransactionFoundException
  {
    Mockito.doReturn(stubbedTransactions)
           .when(transactionRepository)
           .findTransactionsByAccountIdWithOptions(Mockito.anyLong(), Mockito.any(),
                                                   Mockito.any(), Mockito.any());

    TransactionListTransfer actualTransactions = transactionService
        .fetchTransactions(
            new TransactionOptions("keyTest", "date", TransactionType.TRANSACTION_INVALID.toString(), "0", "10",
                                   "false"),
            stubbedAccountId);

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    stubbedTransactions.forEach(t -> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions.getTransactions());
  }

  @Test
  void test_canLimitTransactions() throws NoTransactionFoundException
  {
    int limit = 1;
    Page<Transaction> limitedTransactions = new PageImpl<>(stubbedTransactions.toList().subList(0, limit));

    Mockito.doReturn(limitedTransactions)
           .when(transactionRepository)
           .findTransactionsByAccountIdWithOptions(
               Mockito.anyLong(), Mockito.any(),
               Mockito.any(TransactionType.class), Mockito.any());

    TransactionListTransfer actualTransactions = transactionService
        .fetchTransactions(
            new TransactionOptions("keyTest", "date", TransactionType.TRANSACTION_INVALID.toString(), "0",
                                   Integer.toString(limit), "false"), stubbedAccountId);

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    limitedTransactions.forEach(t -> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions.getTransactions());
  }

  @Test
  void test_canPageTransactions() throws NoTransactionFoundException
  {
    int limit = 1;
    int page = 1;
    Page<Transaction> pagedTransaction = new PageImpl<>(stubbedTransactions.toList().subList(1, 2));

    Mockito.doReturn(pagedTransaction)
           .when(transactionRepository)
           .findTransactionsByAccountIdWithOptions(Mockito.anyLong(), Mockito.any(),
                                                   Mockito.any(TransactionType.class), Mockito.any());

    TransactionListTransfer actualTransactions = transactionService
        .fetchTransactions(
            new TransactionOptions("keyTest", "date", TransactionType.TRANSACTION_INVALID.toString(),
                                   Integer.toString(page), Integer.toString(limit), "false"), stubbedAccountId);

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    pagedTransaction.forEach(t -> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions.getTransactions());
  }

  @Test
  void test_canFetchAccountTransactionById() throws NoTransactionFoundException
  {

    Mockito.doReturn(stubbedTransactionA)
           .when(transactionRepository)
           .findTransactionById(Mockito.anyInt(), Mockito.anyLong());

    TransactionTransfer expected = new TransactionTransfer(stubbedTransactionA);
    TransactionTransfer actual = transactionService.fetchAccountTransactionById(Optional.of(1), stubbedAccountId);

    assertEquals(expected, actual);

  }

  @Test
  void test_willThrowNoTransactionFoundException()
  {
    Page<Transaction> stubbedPagedTransaction = new PageImpl<>(new ArrayList<>());
    Mockito.doReturn(stubbedPagedTransaction).when(transactionRepository).findTransactionsByAccountIdWithOptions(
        Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.any()
    );
    Exception exception = assertThrows(NoTransactionFoundException.class, () ->
      transactionService.fetchTransactions(new TransactionOptions(), stubbedBadAccountId)
    );

    String expectedMessage = "No Transaction Found";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void test_willThrowNoTransactionFoundExceptionOnFetchAccountTransactionById()
  {
    Exception exception = assertThrows(NoTransactionFoundException.class, () ->
        transactionService.fetchAccountTransactionById(Optional.of(1), stubbedBadAccountId)
    );

    String expectedMessage = "No Transaction Found";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void test_willThrowNoTransactionFoundOnFetchTransactions()
  {
    Exception exception = assertThrows(NoTransactionFoundException.class, () ->
        transactionService
            .fetchTransactions(
                new TransactionOptions("keyTest", "date", TransactionType.TRANSACTION_INVALID.toString(), "0", "10",
                                       "false"),
                stubbedBadAccountId)
    );

    String expectedMessage = "No Transaction Found";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void test_willAcceptNullTransactionOptionArgs()
  {
    TransactionOptions options = new TransactionOptions();
    assertEquals("", options.getKeyword());
    assertEquals("date", options.getSortBy());
    assertEquals(TransactionType.TRANSACTION_INVALID, options.getFilter());
    assertEquals(0, options.getOffset());
    assertEquals(10, options.getLimit());
  }

  @Test
  void test_willAcceptTransactionOptionArgs()
  {
    TransactionOptions options = new TransactionOptions("test", null, TransactionType.TRANSACTION_ATM.name(), "2", "5");
    assertEquals("test", options.getKeyword());
    assertEquals("date", options.getSortBy());
    assertEquals(TransactionType.TRANSACTION_ATM, options.getFilter());
    assertEquals(2, options.getOffset());
    assertEquals(5, options.getLimit());
  }
}
