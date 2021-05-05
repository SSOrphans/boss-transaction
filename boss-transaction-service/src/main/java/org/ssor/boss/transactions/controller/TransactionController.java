package org.ssor.boss.transactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssor.boss.core.exception.NoTransactionFoundException;
import org.ssor.boss.transactions.service.TransactionService;
import org.ssor.boss.transactions.transfer.TransactionTransfer;

import javax.websocket.server.PathParam;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = { "api/accounts/{accountId}/transactions" },
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class TransactionController
{
  @Autowired
  TransactionService transactionService;

  @GetMapping(value = "")
  public List<TransactionTransfer> getTransactions(
      @PathParam("keyword") Optional<String> keyword,
      @PathParam("filter") Optional<String> filter,
      @PathParam("offset") Optional<Integer> offset,
      @PathParam("limit") Optional<Integer> limit,
      @PathVariable Optional<Integer> accountId) throws
      NoTransactionFoundException
  {
    List<TransactionTransfer> unsortedTransactions = transactionService.fetchTransactions(keyword, offset, limit, accountId);
    return unsortedTransactions.stream().sorted(Comparator.comparing(TransactionTransfer::getDate).reversed()).collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public TransactionTransfer getTransaction(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> accountId) throws
      NoTransactionFoundException
  {
    return transactionService.fetchAccountTransactionById(id, accountId);
  }
}
