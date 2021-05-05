package org.ssor.boss.transactions.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ssor.boss.core.entity.TransactionType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionOptions
{
  private String keyword;
  private TransactionType filter;
  private Integer offset;
  private Integer limit;


}
