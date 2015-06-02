package com.clouway;

import java.math.BigDecimal;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface ExpensesRepository {
  void add(String id, BigDecimal funds);

  Expense findOne(String id, BigDecimal bigDecimal);
}
