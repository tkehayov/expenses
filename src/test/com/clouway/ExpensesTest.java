package com.clouway;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ExpensesTest {
  private Expenses expenses;

  @Before
  public void setUp() throws Exception {
    expenses = new Expenses();
  }

  @Test
  public void happyPath() {
    Expenses expenses = new Expenses();
    expenses.add("work", new BigDecimal(8));

    assertThat(expenses.getAll().size(), is(1));
    assertThat(expenses.getAll().get("work"), is(new BigDecimal(8)));
  }

  @Test
  public void addExpensesTwice() {
    expenses.add("work", new BigDecimal(3));
    expenses.add("work", new BigDecimal(5));

    assertThat(expenses.getAll().size(), is(1));
    assertThat(expenses.getAll().get("work"), is(new BigDecimal(8)));
  }

  @Test
  public void groupingExpenses() {
    expenses.add("home", new BigDecimal(9));
    expenses.add("work", new BigDecimal(2));

    assertThat(expenses.getAll().size(), is(2));
    assertThat(expenses.getAll().get("home"), is(new BigDecimal(9)));
    assertThat(expenses.getAll().get("work"), is(new BigDecimal(2)));
  }

}
