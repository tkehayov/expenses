package com.clouway.adapter.jdbc;

import com.clouway.adapter.rest.Expense;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface ExpensesRepository {
  void add(String type, String funds);

  List<Expense> find(int numberOfItems, Integer pageNumber);
}
