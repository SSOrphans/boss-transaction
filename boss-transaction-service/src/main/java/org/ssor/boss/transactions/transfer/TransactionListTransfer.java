package org.ssor.boss.transactions.transfer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class TransactionListTransfer
{

  private final List<TransactionTransfer> transactions;
  private final Integer page;
  private final Integer pages;
  private final Integer limit;

}
