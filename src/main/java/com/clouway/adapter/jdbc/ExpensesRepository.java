package com.clouway.adapter.jdbc;

import com.clouway.core.Expense;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface ExpensesRepository {
  void add(String id, String type, String funds);

  Expense findOne(String id, String bigDecimal);
}
