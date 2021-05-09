package org.ssor.boss.transactions.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.ssor.boss.core.entity.TransactionType;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class TransactionOptions
{
  private String keyword;
  private String sortBy;
  private TransactionType filter;
  private Integer offset;
  private Integer limit;
  private Boolean sortDirection;

  public TransactionOptions(String... options)
  {
    int length = options.length;
    this.keyword = length > 0 ? Optional.ofNullable(options[0]).orElse("") : "";
    this.sortBy = length > 1 ? Optional.ofNullable(options[1]).orElse("date") : "date";
    this.filter = length > 2
                  ? TransactionType.valueOf(Optional.ofNullable(options[2]).orElse("TRANSACTION_INVALID"))
                  : TransactionType.TRANSACTION_INVALID;
    this.offset = length > 3 ? Integer.parseInt(Optional.ofNullable(options[3]).orElse("0")) : 0;
    this.limit = length > 4 ? Integer.parseInt(Optional.ofNullable(options[4]).orElse("10")) : 10;
    this.sortDirection = length > 5 && Boolean.parseBoolean(Optional.ofNullable(options[5]).orElse("false"));
  }
}
