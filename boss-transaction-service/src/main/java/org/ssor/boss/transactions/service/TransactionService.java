package org.ssor.boss.transactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ssor.boss.core.entity.Transaction;
import org.ssor.boss.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService
{
  @Autowired
  TransactionRepository transactionRepository;

  public Transaction fetchAccountTransactionById(Optional<Integer> id, Optional<Integer> accountId)
      throws NoTransactionFoundException
  {
    Optional<Transaction> transaction =
        Optional.ofNullable(
            transactionRepository.findTransactionById(id.orElseThrow(NoTransactionFoundException::new),
                                                      accountId.orElseThrow(NoTransactionFoundException::new)));
    return transaction.orElseThrow(NoTransactionFoundException::new);
  }


  public List<Transaction> fetchTransactions(Optional<String> keyword, Optional<Integer> offset, Optional<Integer> limit, Optional<Integer> accountId)
      throws NoTransactionFoundException
  {
    List<Transaction> transactions;
    Pageable pageable = PageRequest.of(offset.orElse(0), limit.orElse(5));
    if (keyword.isEmpty())
      transactions = transactionRepository
          .findTransactionsByAccountId(accountId.orElseThrow(NoTransactionFoundException::new), pageable);
    else
      transactions = transactionRepository
          .findTransactionsByAccountIdLikeMerchantName(accountId.orElseThrow(NoTransactionFoundException::new),
                                                       keyword.get(), pageable);

    if (transactions.isEmpty())
      throw new NoTransactionFoundException();

    return transactions;
  }
}
