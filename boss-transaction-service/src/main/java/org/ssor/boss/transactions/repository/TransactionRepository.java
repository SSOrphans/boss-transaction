package org.ssor.boss.transactions.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.ssor.boss.core.entity.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>
{
  @Query(value = "SELECT t FROM Transaction t WHERE t.accountId = :accountId AND t.id = :id")
  Transaction findTransactionById(Integer id, Integer accountId);

  @Query(value = "SELECT t FROM Transaction t WHERE t.accountId = :accountId")
  List<Transaction> findTransactionsByAccountId(Integer accountId, Pageable pageable);

  @Query(value = "SELECT t FROM Transaction t WHERE  t.accountId = :accountId AND lower(t.merchantName) like lower(concat('%',:keyword,'%'))")
  List<Transaction> findTransactionsByAccountIdLikeMerchantName(Integer accountId, String keyword, Pageable pageable);
}
