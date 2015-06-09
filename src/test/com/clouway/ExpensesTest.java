package com.clouway;

import com.clouway.adapter.http.ExpensesPage;
import com.clouway.adapter.jdbc.ExpensesRepository;
import com.clouway.adapter.jdbc.PersistenceExpensesRepository;
import com.clouway.adapter.rest.Expense;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sitebricks.client.transport.Json;
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

import java.util.List;

import static com.google.inject.util.Providers.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ExpensesTest {
  private LocalServiceTestHelper dataStore = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private DatastoreService service;
  private ExpensesRepository repository;

  @Mock
  public Request request;

  @Mock
  public RequestRead requestRead;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() {
    dataStore.setUp();
    service = DatastoreServiceFactory.getDatastoreService();
    repository = new PersistenceExpensesRepository(of(service));
  }

  @After
  public void after() {
    dataStore.tearDown();
  }

  @Test
  public void happyPath() {
    String funds = "44444";
    Expense expenses = new Expense("types", funds);
    ExpensesPage page = mockExpenses(expenses);

    page.add(request);

    List<Expense> one = repository.find(1, 1);

    assertThat(one.get(0).getExpenses(), is(funds));
  }

  @Test
  public void addInvalidFunds() {
    final Expense expenses = new Expense("type", "invalid funds");
    ExpensesPage page = mockExpenses(expenses);

    page.add(request);
    List<Expense> one = repository.find(1, 1);

    assertTrue(one.isEmpty());
  }

  @Test
  public void addNegativeFunds() {
    final Expense expenses = new Expense("type2", "-23");
    ExpensesPage page = mockExpenses(expenses);

    page.add(request);
    List<Expense> one = repository.find(1, 1);

    assertTrue(one.isEmpty());
  }

  private ExpensesPage mockExpenses(final Expense expenses) {
    ExpensesPage page = new ExpensesPage(of(repository));

    context.checking(new Expectations() {{
      oneOf(request).read(Expense.class);
      will(returnValue(requestRead));
      oneOf(requestRead).as(Json.class);
      will(returnValue(expenses));
    }});

    return page;
  }
}