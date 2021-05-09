package org.ssor.boss.transactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.core.entity.TransactionType;
import org.ssor.boss.transactions.service.TransactionOptions;
import org.ssor.boss.transactions.service.TransactionService;
import org.ssor.boss.transactions.transfer.TransactionListTransfer;
import org.ssor.boss.transactions.transfer.TransactionTransfer;

import javax.websocket.server.PathParam;
import java.util.Optional;

@RestController
@RequestMapping(value = { "api/v1/accounts/{accountId}/transactions" },
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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
      @PathVariable Optional<Integer> accountId) throws
      NoTransactionFoundException, ArrayIndexOutOfBoundsException
  {
    TransactionType typeFilter = TransactionType.values()[filter.orElse(0)];
    TransactionOptions options = new TransactionOptions(
        keyword.orElse(""),
        sortBy.orElse("date"),
        typeFilter.toString(),
        offset.orElse(0).toString(),
        limit
            .map(optLimitNotZero -> optLimitNotZero < 1? 1 : optLimitNotZero)
            .orElse(5).toString(),
        "false");
    return transactionService.fetchTransactions(options, accountId);
  }

  @GetMapping("/{id}")
  public TransactionTransfer getTransaction(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> accountId) throws
      NoTransactionFoundException
  {
    return transactionService.fetchAccountTransactionById(id, accountId);
  }
}
