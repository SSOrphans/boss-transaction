package org.ssor.boss.transactions.transfer;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ErrorMessage
{
  private final HttpStatus status;
  private final String message;
}
