package org.ssor.boss.transactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssor.boss.entity.Transaction;
import org.ssor.boss.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService
{
  @Autowired
  TransactionRepository transactionRepository;


  public Transaction fetchAccountTransactionById(Optional<Integer> id, Optional<Integer> accountId) throws
      NoTransactionFoundException
  {
    Optional<Transaction> savingTransaction =
        Optional.ofNullable(
            transactionRepository.findTransactionById(id.orElseThrow(NoTransactionFoundException::new), accountId.orElseThrow(NoTransactionFoundException::new)));
    return savingTransaction.orElseThrow(NoTransactionFoundException::new);
  }

  public List<Transaction> fetchTransactions(Optional<String> keyword, Optional<Integer> accountId) throws
      NoTransactionFoundException
  {
    List<Transaction> transactions;
    if (keyword.isEmpty())
    {
      transactions = transactionRepository
          .findTransactionsByAccountId(accountId.orElseThrow(NoTransactionFoundException::new));
    }
    else
    {
      transactions = transactionRepository
          .findTransactionsByAccountIdLikeMerchantName(accountId.orElseThrow(NoTransactionFoundException::new),
                                                       keyword.get());
    }

    if (transactions.isEmpty())
    {
      throw new NoTransactionFoundException();
    }

    return transactions;
  }
}
