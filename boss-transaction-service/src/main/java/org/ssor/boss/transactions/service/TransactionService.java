package org.ssor.boss.transactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ssor.boss.core.entity.Transaction;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.repository.TransactionRepository;
import org.ssor.boss.transactions.transfer.TransactionTransfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService
{
  @Autowired
  TransactionRepository transactionRepository;

  public TransactionTransfer fetchAccountTransactionById(Optional<Integer> id, Optional<Integer> accountId)
      throws NoTransactionFoundException
  {
    Optional<Transaction> transaction =
        Optional.ofNullable(
            transactionRepository.findTransactionById(id.orElseThrow(NoTransactionFoundException::new),
                                                      accountId.orElseThrow(NoTransactionFoundException::new)));
    return new TransactionTransfer(transaction.orElseThrow(NoTransactionFoundException::new));
  }


  public List<TransactionTransfer> fetchTransactions(TransactionOptions options, Optional<Integer> accountId)
      throws NoTransactionFoundException
  {
    final List<TransactionTransfer> transactions = new ArrayList<>();
    List<Transaction> rawTransactions;
    Pageable pageable = PageRequest.of(options.getOffset(), options.getLimit());

    if (options.getKeyword().isEmpty())
    {
       rawTransactions = transactionRepository
          .findTransactionsByAccountId(accountId.orElseThrow(NoTransactionFoundException::new), pageable);
    }
    else
    {
      rawTransactions = transactionRepository
          .findTransactionsByAccountIdLikeMerchantName(accountId.orElseThrow(NoTransactionFoundException::new),
                                                       options.getKeyword(), pageable);
    }

    rawTransactions.forEach(raw -> transactions.add(new TransactionTransfer(raw)));

    if (transactions.isEmpty())
      throw new NoTransactionFoundException();

    return transactions;
  }
}
