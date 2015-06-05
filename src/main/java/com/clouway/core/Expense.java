package com.clouway.core;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class Expense {
  private final String funds;

  public Expense(String funds) {
    this.funds = funds;
  }

  public String getFunds() {
    return funds;
  }
}
