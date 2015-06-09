package com.clouway.adapter.db;

import com.clouway.adapter.rest.Expense;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface ExpensesRepository {
  void add(String type, String funds);

  List<Expense> find(int pageSize, Integer pageNumber);
}
