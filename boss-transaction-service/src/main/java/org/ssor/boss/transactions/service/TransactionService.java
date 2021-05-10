package org.ssor.boss.transactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.ssor.boss.core.entity.Transaction;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.repository.TransactionRepository;
import org.ssor.boss.transactions.transfer.TransactionListTransfer;
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


  public TransactionListTransfer fetchTransactions(TransactionOptions options, Optional<Integer> accountId)
      throws NoTransactionFoundException
  {
    Pageable pageable = PageRequest.of(options.getOffset(), options.getLimit(), Sort.by(options.getSortDirection(), options.getSortBy(), "date"));
    Optional<Page<Transaction>> optionalTransactions = Optional.ofNullable(transactionRepository.findTransactionsByAccountIdWithOptions(
        accountId.orElseThrow(NoTransactionFoundException::new), options.getKeyword(), options.getFilter(), pageable
    ));

    Page<Transaction> transactions = optionalTransactions.orElseThrow(NoTransactionFoundException::new);

    List<TransactionTransfer> transactionTransfers = transactions.stream().map(TransactionTransfer::new).collect(Collectors.toList());

    if (transactionTransfers.isEmpty())
      throw new NoTransactionFoundException();

    return new TransactionListTransfer(transactionTransfers, transactions.getNumber() + 1 ,transactions.getTotalPages(), options.getLimit());
  }
}
