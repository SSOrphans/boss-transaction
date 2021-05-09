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
public class TransactionServiceTest
{

  @MockBean
  private TransactionRepository transactionRepository;
  @InjectMocks
  private TransactionService transactionService;

  private static Transaction stubbedTransactionA;
  private static Transaction stubbedTransactionB;
  private static Page<Transaction> stubbedTransactions;

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
    transactionA.setType(TransactionType.TRANSACTION_PAYMENT);

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
           .findTransactionsByAccountIdWithOptions(Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.any());

    TransactionListTransfer actualTransactions = transactionService
        .fetchTransactions(new TransactionOptions("", "date", TransactionType.TRANSACTION_INVALID.toString(), "0", "10", "false"),
                           Optional.of(1));

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    stubbedTransactions.forEach(t -> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions.getTransactions());
  }

  @Test
  void test_fetchTransactionsWithKeyword() throws NoTransactionFoundException
  {
    Mockito.doReturn(stubbedTransactions)
           .when(transactionRepository)
           .findTransactionsByAccountIdWithOptions(Mockito.anyInt(), Mockito.any(),
                                                   Mockito.any(), Mockito.any());

    TransactionListTransfer actualTransactions = transactionService
        .fetchTransactions(new TransactionOptions("keyTest", "date", TransactionType.TRANSACTION_INVALID.toString(), "0", "10", "false"),
                           Optional.of(1));

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    stubbedTransactions.forEach(t -> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions.getTransactions());
  }

  @Test
  void test_canLimitTransactions() throws NoTransactionFoundException
  {
    Integer limit = 1;
    Page<Transaction> limitedTransactions = new PageImpl<>(stubbedTransactions.toList().subList(0, limit));

    Mockito.doReturn(limitedTransactions)
           .when(transactionRepository)
           .findTransactionsByAccountIdWithOptions(Mockito.anyInt(), Mockito.any(),
                                                   Mockito.any(TransactionType.class), Mockito.any());

    TransactionListTransfer actualTransactions = transactionService
        .fetchTransactions(new TransactionOptions("keyTest", "date", TransactionType.TRANSACTION_INVALID.toString(), "0", limit.toString(), "false"),
                           Optional.of(1));

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    limitedTransactions.forEach(t -> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions.getTransactions());
  }

  @Test
  void test_canPageTransactions() throws NoTransactionFoundException
  {
    Integer limit = 1;
    Integer page = 1;
    Page<Transaction> pagedTransaction = new PageImpl<>(stubbedTransactions.toList().subList(1, 2));

    Mockito.doReturn(pagedTransaction)
           .when(transactionRepository)
           .findTransactionsByAccountIdWithOptions(Mockito.anyInt(), Mockito.any(),
                                                   Mockito.any(TransactionType.class), Mockito.any());

    TransactionListTransfer actualTransactions = transactionService
        .fetchTransactions(new TransactionOptions("keyTest", "date", TransactionType.TRANSACTION_INVALID.toString(), page.toString(), limit.toString(), "false"),
                           Optional.of(1));

    List<TransactionTransfer> expectedTransactions = new ArrayList<>();

    pagedTransaction.forEach(t -> expectedTransactions.add(new TransactionTransfer(t)));

    assertEquals(expectedTransactions, actualTransactions.getTransactions());
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
            .fetchTransactions(new TransactionOptions("keyTest", "date", TransactionType.TRANSACTION_INVALID.toString(), "0", "10", "false"),
                               Optional.of(-1))
    );

    String expectedMessage = "No Transaction Found";
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void test_willAcceptNullTransactionOptionArgs()
  {
    TransactionOptions options = new TransactionOptions();
    assertEquals("",options.getKeyword());
    assertEquals("date",options.getSortBy());
    assertEquals(TransactionType.TRANSACTION_INVALID, options.getFilter());
    assertEquals(0,options.getOffset());
    assertEquals(10,options.getLimit());
  }

  @Test
  void test_willAcceptTransactionOptionArgs()
  {
    TransactionOptions options = new TransactionOptions("test", null, TransactionType.TRANSACTION_ATM.name(), "2", "5");
    assertEquals("test",options.getKeyword());
    assertEquals("date",options.getSortBy());
    assertEquals(TransactionType.TRANSACTION_ATM, options.getFilter());
    assertEquals(2,options.getOffset());
    assertEquals(5,options.getLimit());
  }

}
