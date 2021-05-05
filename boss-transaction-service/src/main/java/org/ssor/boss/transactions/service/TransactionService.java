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
import java.util.stream.Collectors;

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
    Pageable pageable = PageRequest.of(options.getOffset(), options.getLimit());
    List<Transaction> transactions = transactionRepository.findTransactionsByAccountIdWithOptions(
        accountId.orElseThrow(NoTransactionFoundException::new), options.getKeyword(), options.getFilter(), pageable
    );

    List<TransactionTransfer> transactionTransfers = transactions.stream().map(TransactionTransfer::new).collect(Collectors.toList());

    if (transactionTransfers.isEmpty())
      throw new NoTransactionFoundException();

    return transactionTransfers;
  }
}
