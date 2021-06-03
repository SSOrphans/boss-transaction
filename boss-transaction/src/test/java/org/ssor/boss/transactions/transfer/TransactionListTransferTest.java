package org.ssor.boss.transactions.transfer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TransactionListTransferTest
{
  @Test
  void test_canCreateTransactionListTransfer()
  {
    List<TransactionTransfer> list = new ArrayList<>();
    TransactionListTransfer transactionListTransfer = new TransactionListTransfer(list, 1, 1, 1);
    assertNotNull(transactionListTransfer);
  }

  @Test
  void test_canGetPropertiesOfTransactionListTransfer()
  {
    List<TransactionTransfer> list = new ArrayList<>();
    TransactionListTransfer transactionListTransfer = new TransactionListTransfer(list, 1, 2, 3);
    assertTrue(transactionListTransfer.getTransactions().isEmpty());
    assertEquals(1, transactionListTransfer.getPage());
    assertEquals(2, transactionListTransfer.getPages());
    assertEquals(3, transactionListTransfer.getLimit());
  }

}
