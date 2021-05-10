package org.ssor.boss.transactions.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.ssor.boss.core.entity.TransactionType;

import java.util.Optional;

@Getter
public class TransactionOptions
{
  private final String keyword;
  private final String sortBy;
  private final TransactionType filter;
  private final Integer offset;
  private final Integer limit;
  private final Sort.Direction sortDirection;

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
    String directionToggle = Optional.ofNullable(options[5]).orElse("DESC");
    this.sortDirection = directionToggle.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
  }
}
