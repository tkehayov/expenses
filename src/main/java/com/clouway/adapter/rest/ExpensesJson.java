package com.clouway.adapter.rest;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ExpensesJson {
  private String id;
  private String type;
  private String expenses;

  public ExpensesJson() {

  }

  public ExpensesJson(String id, String type, String expenses) {
    this.id = id;
    this.type = type;
    this.expenses = expenses;
  }

  public String getExpenses() {
    return expenses;
  }

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }
}