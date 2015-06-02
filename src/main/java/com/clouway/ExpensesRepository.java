package com.clouway;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface ExpensesRepository {
  void add(String id, String funds);

  Expense findOne(String id, String bigDecimal);
}
