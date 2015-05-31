package com.clouway;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableMap;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class Expenses {
  private final Map<String, BigDecimal> expense = new HashMap<String, BigDecimal>();

  public Map<String, BigDecimal> add(String type, BigDecimal funds) {
    BigDecimal currentFunds = expense.get(type);

    if (currentFunds != null) {
      BigDecimal fundsToAdd = currentFunds.add(funds);
      expense.put(type, fundsToAdd);
      return expense;
    }

    currentFunds = new BigDecimal(funds.toString());
    expense.put(type, currentFunds);

    return expense;
  }

  public Map<String, BigDecimal> getAll() {
    return expense;
  }
}
