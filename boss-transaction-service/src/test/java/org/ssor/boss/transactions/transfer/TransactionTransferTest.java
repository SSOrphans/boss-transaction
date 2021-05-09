package org.ssor.boss.transactions.transfer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class TransactionTransferTest
{

  @Test
  void test_canModifyTransactionTransfer(){
    TransactionTransfer transfer = new TransactionTransfer();
    LocalDateTime now = LocalDateTime.now();
    transfer.setAmount(123.45f);
    transfer.setBalance(1234.56f);
    transfer.setDate(now);
    transfer.setId(1);
    transfer.setPending(true);
    transfer.setMerchant("TestMerchant");
    transfer.setType("TestType");

    assertEquals(1, transfer.getId());
    assertEquals(now, transfer.getDate());
    assertEquals(123.45f, transfer.getAmount());
    assertEquals(1234.56f, transfer.getBalance());
    assertEquals(true, transfer.getPending());
    assertEquals("TestMerchant", transfer.getMerchant());
    assertEquals("TestType", transfer.getType());
  }
}
