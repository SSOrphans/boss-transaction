package org.ssor.boss.transactions.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionOptions
{
  private String keyword;
  private String filter;
  private Integer offset;
  private Integer limit;
}
