package com.clouway.adapter.rest;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class Expense {
  private String type;
  private String expenses;

  public Expense() {

  }

  public Expense(String type, String expenses) {
    this.type = type;
    this.expenses = expenses;
  }

  public String getExpenses() {
    return expenses;
  }

  public String getType() {
    return type;
  }
}