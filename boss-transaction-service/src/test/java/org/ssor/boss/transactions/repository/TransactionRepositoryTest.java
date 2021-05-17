package org.ssor.boss.transactions.repository;

import org.hibernate.annotations.Filter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.ssor.boss.core.entity.TransactionType;
import org.ssor.boss.core.entity.Transaction;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest
{

  @Autowired
  public TransactionRepository transactionRepository;

  @Test
  void test_repositoryNotNull()
  {
    assertNotNull(transactionRepository);
  }

  @Test
  void test_canFindTransaction()
  {
    assertNotNull(transactionRepository.findTransactionById(1, 2L));
  }

  @Test
  void test_fetchTransactionsWithFilter()
  {
    Page<Transaction> actual = transactionRepository
        .findTransactionsByAccountIdWithOptions(2L, "", TransactionType.TRANSACTION_DEPOSIT, PageRequest.of(0, 10));
    assertFalse(actual.isEmpty());
  }

  @Test
  void test_fetchTransactionsWithWrongFilter()
  {
    Page<Transaction> actual = transactionRepository
        .findTransactionsByAccountIdWithOptions(2L, "", TransactionType.TRANSACTION_CHECK, PageRequest.of(0, 10));
    assertTrue(actual.isEmpty());
  }

  @Test
  void test_fetchTransactionsWithoutFilter()
  {
    Page<Transaction> actual = transactionRepository
        .findTransactionsByAccountIdWithOptions(2L, "", TransactionType.TRANSACTION_INVALID, PageRequest.of(0, 10));
    assertFalse(actual.isEmpty());
  }

  @Test
  void test_fetchTransactionsWithKeyword()
  {
    Page<Transaction> actual = transactionRepository
        .findTransactionsByAccountIdWithOptions(2L, "te", TransactionType.TRANSACTION_INVALID, PageRequest.of(0, 10));
    assertFalse(actual.isEmpty());
  }

  @Test
  void test_fetchTransactionsWithWrongKeyword()
  {
    Page<Transaction> actual = transactionRepository
        .findTransactionsByAccountIdWithOptions(2L, "tu", TransactionType.TRANSACTION_INVALID, PageRequest.of(0, 10));
    assertTrue(actual.isEmpty());
  }

  @Test
  void test_fetchTransactionsWithoutKeyword()
  {
    Page<Transaction> actual = transactionRepository
        .findTransactionsByAccountIdWithOptions(2L, "", TransactionType.TRANSACTION_INVALID, PageRequest.of(0, 10));
    assertFalse(actual.isEmpty());
  }

  @Test
  void test_canFindTransactionsByAccountId()
  {
    Page<Transaction> transactions = transactionRepository
        .findTransactionsByAccountIdWithOptions(2L, "", TransactionType.TRANSACTION_INVALID,
                                                PageRequest.of(0, 5));
    assertFalse(transactions.isEmpty());
  }

  @Test
  void test_canSearchTransactions()
  {
    Page<Transaction> transactions = transactionRepository
        .findTransactionsByAccountIdWithOptions(2L, "test", TransactionType.TRANSACTION_INVALID,
                                                PageRequest.of(0, 5));
    assertFalse(transactions.isEmpty());
  }
}
