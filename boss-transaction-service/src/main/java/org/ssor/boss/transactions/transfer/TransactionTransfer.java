package org.ssor.boss.transactions.transfer;

import lombok.*;
import org.ssor.boss.core.entity.Transaction;
import org.ssor.boss.core.entity.TransactionType;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class TransactionTransfer
{
  @NonNull
  private Integer id;
  private Float amount;
  private Float balance;
  private String merchant;
  private LocalDateTime date;
  private String type;
  private boolean pending;

  public TransactionTransfer(Transaction transaction)
  {
    setEntity(transaction);
  }

  public void setEntity(Transaction transaction)
  {
    this.id = transaction.getId();
    this.amount = transaction.getAmount();
    this.balance = transaction.getNewBalance();
    this.merchant = transaction.getMerchantName();
    this.date = transaction.getDate();
    this.pending = transaction.getPending();
    this.type = transaction.getType().toString();
  }

}
