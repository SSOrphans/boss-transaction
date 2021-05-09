package org.ssor.boss.transactions.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.ssor.boss.core.entity.TransactionType;
import org.ssor.boss.transactions.BossControllerApplicationTests;
import org.ssor.boss.core.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = { BossControllerApplicationTests.class })
@TestPropertySource(value = {
    "classpath:test.properties"
})
class TransactionRepositoryTest
{

  @Autowired
  public TransactionRepository transactionRepository;

  private static Transaction stubbedTransactionA;
  private static Transaction stubbedTransactionB;

  @BeforeAll
  static void setup()
  {
    stubbedTransactionA = new Transaction();
    stubbedTransactionA.setId(999);
    stubbedTransactionA.setAccountId(999);
    stubbedTransactionA.setAtmTransactionId(999);
    stubbedTransactionA.setAmount(123.56f);
    stubbedTransactionA.setNewBalance(12345.67f);
    stubbedTransactionA.setDate(LocalDateTime.now());
    stubbedTransactionA.setMerchantName("TestMerchant");
    stubbedTransactionA.setPending(false);
    stubbedTransactionA.setSucceeded(true);
    stubbedTransactionA.setOverdraftId(null);
    stubbedTransactionA.setType(TransactionType.TRANSACTION_DEPOSIT);

    stubbedTransactionB = new Transaction();
    stubbedTransactionB.setId(2);
    stubbedTransactionB.setAccountId(2);
    stubbedTransactionB.setAtmTransactionId(2);
    stubbedTransactionB.setAmount(543.21f);
    stubbedTransactionB.setNewBalance(98754.32f);
    stubbedTransactionB.setDate(LocalDateTime.now());
    stubbedTransactionB.setMerchantName("TestMerchant");
    stubbedTransactionB.setPending(true);
    stubbedTransactionB.setSucceeded(true);
    stubbedTransactionB.setOverdraftId(null);
    stubbedTransactionB.setType(TransactionType.TRANSACTION_PAYMENT);

  }

  @Test
  void test_canSaveTransaction()
  {
    Transaction transaction = transactionRepository.save(stubbedTransactionA);
    assertNotNull(transaction);
  }

  @Test
  void test_canFindTransaction()
  {
    assertNotNull(transactionRepository.findTransactionById(1, 2));
  }

  @Test
  void test_fetchTransactionsWithFilter()
  {
    Page<Transaction> actual = transactionRepository.findTransactionsByAccountIdWithOptions(1, "", TransactionType.TRANSACTION_DEPOSIT, PageRequest.of(0, 10));
    assertFalse(actual.isEmpty());
  }

  @Test
  void test_fetchTransactionsWithWrongFilter()
  {
    Page<Transaction> actual = transactionRepository.findTransactionsByAccountIdWithOptions(1, "", TransactionType.TRANSACTION_CHECK, PageRequest.of(0, 10));
    assertTrue(actual.isEmpty());
  }

  @Test
  void test_fetchTransactionsWithoutFilter()
  {
    Page<Transaction> actual = transactionRepository.findTransactionsByAccountIdWithOptions(1, "", TransactionType.TRANSACTION_INVALID, PageRequest.of(0, 10));
    assertFalse(actual.isEmpty());
  }

  @Test
  void test_fetchTransactionsWithKeyword()
  {
    Page<Transaction> actual = transactionRepository.findTransactionsByAccountIdWithOptions(1, "te", TransactionType.TRANSACTION_INVALID, PageRequest.of(0, 10));
    assertFalse(actual.isEmpty());
  }

  @Test
  void test_fetchTransactionsWithWrongKeyword()
  {
    Page<Transaction> actual = transactionRepository.findTransactionsByAccountIdWithOptions(1, "tu", TransactionType.TRANSACTION_INVALID, PageRequest.of(0, 10));
    assertTrue(actual.isEmpty());
  }

  @Test
  void test_fetchTransactionsWithoutKeyword()
  {
    Page<Transaction> actual = transactionRepository.findTransactionsByAccountIdWithOptions(1, "", TransactionType.TRANSACTION_INVALID, PageRequest.of(0, 10));
    assertFalse(actual.isEmpty());
  }

  @Test
  void test_canFindTransactionsByAccountId()
  {
    transactionRepository.save(stubbedTransactionA);
    Page<Transaction> transactions = transactionRepository
        .findTransactionsByAccountIdWithOptions(stubbedTransactionA.getAccountId(), "", TransactionType.TRANSACTION_INVALID,
                                     PageRequest.of(0, 5));
    assertFalse(transactions.isEmpty());
  }

  @Test
  void test_canSearchTransactions()
  {
    transactionRepository.save(stubbedTransactionA);
    Page<Transaction> transactions = transactionRepository
        .findTransactionsByAccountIdWithOptions(stubbedTransactionA.getAccountId(), "test", stubbedTransactionA.getType(),
                                                PageRequest.of(0, 5));
    assertFalse(transactions.isEmpty());
  }
}
