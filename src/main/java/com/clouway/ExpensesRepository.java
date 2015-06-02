package com.clouway;

import java.math.BigDecimal;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface ExpensesRepository {
  void add(BigDecimal funds);

  Expense findOne(BigDecimal bigDecimal);
}
