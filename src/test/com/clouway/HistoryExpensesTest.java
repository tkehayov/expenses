package com.clouway;

import com.clouway.adapter.http.HistoryExpensesPage;
import com.clouway.adapter.jdbc.ExpensesRepository;
import com.clouway.adapter.jdbc.PersistenceExpensesRepository;
import com.clouway.adapter.rest.Expense;
import com.clouway.adapter.rest.PageItems;
import com.clouway.core.InvalidPageNumberException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Providers;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.conversion.Converter;
import com.google.sitebricks.conversion.ConverterRegistry;
import com.google.sitebricks.conversion.StandardTypeConverter;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Request.RequestRead;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.util.Providers.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class HistoryExpensesTest {
  private final LocalServiceTestHelper dataStore = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private DatastoreService service;
  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  ExpensesRepository repository;

  @Mock
  private RequestRead requestRead;

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

    HistoryExpensesPage historyExpensesPage = new HistoryExpensesPage(of(repository));

    historyExpensesPage.getItems(2, 2);
    List<Expense> one = repository.find(1, 2);

    assertThat(one.size(), is(1));
  }

  @Test
  public void getItemsFromNextPage() {
    addExpenses(new Expense("vacations", "44"), new Expense("girls", "5"), new Expense("beer", "999"));

    HistoryExpensesPage historyExpensesPage = new HistoryExpensesPage(of(repository));
    historyExpensesPage.getItems(1, 2);
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
