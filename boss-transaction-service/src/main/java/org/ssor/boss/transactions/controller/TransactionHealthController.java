package org.ssor.boss.transactions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "api/v1/transactions/health" })
public class TransactionHealthController
{
  @GetMapping(value = "")
  public ResponseEntity<String> transactionHealth(){
    return new ResponseEntity<>("Healthy", HttpStatus.OK);
  }
}
