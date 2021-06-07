package org.ssor.boss.transactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.ssor.boss.core.entity.TransactionType;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.service.TransactionOptions;
import org.ssor.boss.transactions.service.TransactionService;
import org.ssor.boss.transactions.transfer.TransactionListTransfer;
import org.ssor.boss.transactions.transfer.TransactionTransfer;

import javax.websocket.server.PathParam;
import java.util.Optional;

@RestController
@RequestMapping(value = { "api/v1/accounts/{accountId}/transactions" },
                produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@CrossOrigin
public class TransactionController
{
  @Autowired
  TransactionService transactionService;

  @GetMapping(value = "")
  public TransactionListTransfer getTransactions(
      @PathParam("keyword") Optional<String> keyword,
      @PathParam("filter") Optional<Integer> filter,
      @PathParam("offset") Optional<Integer> offset,
      @PathParam("limit") Optional<Integer> limit,
      @PathParam("sortBy") Optional<String> sortBy,
      @PathParam("sortDirection") Optional<String> sortDirection,
      @PathVariable Optional<Long> accountId) throws
      NoTransactionFoundException, ArrayIndexOutOfBoundsException
  {
    TransactionType typeFilter = TransactionType.values()[filter.orElse(0)];
    var options = new TransactionOptions(
        keyword.orElse(""),
        sortBy.orElse("date"),
        typeFilter.toString(),
        offset.orElse(0).toString(),
        limit
            .map(optLimitNotZero -> optLimitNotZero < 1 ? 1 : optLimitNotZero)
            .orElse(5).toString(),
        sortDirection.orElse("false"));
    return transactionService.fetchTransactions(options, accountId);
  }

  @GetMapping("/{id}")
  public TransactionTransfer getTransaction(@PathVariable Optional<Integer> id,
                                            @PathVariable Optional<Long> accountId) throws
      NoTransactionFoundException
  {
    return transactionService.fetchAccountTransactionById(id, accountId);
  }
}
