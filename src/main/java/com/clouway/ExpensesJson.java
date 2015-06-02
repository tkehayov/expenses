package com.clouway;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
class ExpensesJson {
  private String id;
  private String expenses;

  public ExpensesJson() {

  }

  public ExpensesJson(String id, String expenses) {
    this.id = id;
    this.expenses = expenses;
  }

  public String getExpenses() {
    return expenses;
  }

  public String getId() {
    return id;
  }
}
