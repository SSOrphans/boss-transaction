package org.ssor.boss.transactions.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.ssor.boss.transactions.BossControllerApplicationTests;
import org.ssor.boss.core.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

  }

  @Test
  void test_canFindTransaction()
  {
    assertNotNull(transactionRepository.findTransactionById(1, 1));
    assertNotNull(transactionRepository);
  }

  @Test
  void test_canSaveTransaction()
  {
    Transaction transaction = transactionRepository.save(stubbedTransactionA);
    assertNotNull(transaction);
  }

  @Test
  void test_canFindTransactionsByAccountId()
  {
    transactionRepository.save(stubbedTransactionA);
    List<Transaction> transactions = transactionRepository
        .findTransactionsByAccountId(stubbedTransactionA.getAccountId(),
                                     PageRequest.of(0, 5));
    assertFalse(transactions.isEmpty());
  }

  @Test
  void test_canSearchTransactions()
  {
    transactionRepository.save(stubbedTransactionA);
    List<Transaction> transactions = transactionRepository
        .findTransactionsByAccountIdLikeMerchantName(stubbedTransactionA.getAccountId(), "test",
                                     PageRequest.of(0, 5));
    assertFalse(transactions.isEmpty());
  }
}
