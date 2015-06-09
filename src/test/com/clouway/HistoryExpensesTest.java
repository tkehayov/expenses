package com.clouway;

import com.clouway.adapter.jdbc.ExpensesRepository;
import com.clouway.adapter.jdbc.PersistenceExpensesRepository;
import com.clouway.adapter.rest.Expense;
import com.clouway.core.InvalidPageNumberException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.util.Providers;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class HistoryExpensesTest {
  private final LocalServiceTestHelper dataStore = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private DatastoreService service;
  @Rule
  private ExpectedException exception = ExpectedException.none();

  @Rule
  private JUnitRuleMockery context = new JUnitRuleMockery();

  private ExpensesRepository repository;


  @Before
  public void setUp() throws Exception {
    dataStore.setUp();
    service = DatastoreServiceFactory.getDatastoreService();
    repository = new PersistenceExpensesRepository(Providers.of(service));
  }

  @After
  public void tearDown() throws Exception {
    dataStore.tearDown();
  }

  @Test
  public void happyPath() {
    addExpenses(new Expense("women", "51"), new Expense("cars", "1"), new Expense("girls", "4"));

    List<Expense> one = repository.find(1, 2);

    assertThat(one.size(), is(1));
  }

  @Test
  public void getItemsFromNextPage() {
    addExpenses(new Expense("vacations", "44"), new Expense("girls", "5"), new Expense("beer", "999"));

    List<Expense> one = repository.find(2, 2);

    assertThat(one.size(), is(1));
  }

  @Test
  public void getItemsFromNegativePage() {
    exception.expect(InvalidPageNumberException.class);
    addExpenses(new Expense("dance", "10"), new Expense("girls", "51"), new Expense("vodka", "2"));

    repository.find(2, -2);
  }

  private void addExpenses(Expense... expenses) {
    for (Expense expense : expenses) {
      repository.add(expense.getType(), expense.getExpenses());
    }
  }
}
