package com.clouway;

import java.math.BigDecimal;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class Expense {
  private final BigDecimal funds;

  public Expense(BigDecimal funds) {
    this.funds = funds;
  }

  public BigDecimal getFunds() {
    return funds;
  }
}
