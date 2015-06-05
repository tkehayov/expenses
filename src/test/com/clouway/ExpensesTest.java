package com.clouway;

import com.clouway.adapter.jdbc.ExpensesRepository;
import com.clouway.adapter.jdbc.PersistenceExpensesRepository;
import com.clouway.adapter.http.ExpensesPage;
import com.clouway.adapter.rest.ExpensesJson;
import com.clouway.core.Expense;
import com.clouway.core.InvalidFundsCastException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.sitebricks.client.Web;
import com.google.sitebricks.client.WebResponse;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.conversion.Converter;
import com.google.sitebricks.conversion.ConverterRegistry;
import com.google.sitebricks.conversion.StandardTypeConverter;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Request.RequestRead;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.clouway.Server.getUrl;
import static com.google.inject.util.Providers.of;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ExpensesTest {
  private final LocalServiceTestHelper dataStore = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private DatastoreService service;

  @Before
  public void setUp() {
    dataStore.setUp();
    service = DatastoreServiceFactory.getDatastoreService();
  }

  @After
  public void after() {
    dataStore.tearDown();
  }

  @Mock
  private Request request;

  @Mock
  private RequestRead requestRead;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void happyPath() {
    String funds = "44444";
    final ExpensesJson expensesJson = new ExpensesJson("george", "type", funds);
    ExpensesPage page = mockExpenses(expensesJson);
    ExpensesRepository repository = new PersistenceExpensesRepository(of(service));

    page.add(request);
    Expense one = repository.findOne(expensesJson.getId(), funds);

    assertTrue(one.getFunds().equals(funds));
  }

  @Test
  @Ignore
  public void addInvalidFunds() {
    exception.expect(InvalidFundsCastException.class);

    final ExpensesJson expensesJson = new ExpensesJson("marin", "type", "invalid funds");
    ExpensesPage page = mockExpenses(expensesJson);
    page.add(request);
  }

  private ExpensesPage mockExpenses(final ExpensesJson expensesJson) {
    ExpensesRepository repository = new PersistenceExpensesRepository(of(service));
    ExpensesPage page = new ExpensesPage(of(repository));

    context.checking(new Expectations() {{
      oneOf(request).read(ExpensesJson.class);
      will(returnValue(requestRead));
      oneOf(requestRead).as(Json.class);
      will(returnValue(expensesJson));
    }});
    return page;
  }

  /*
  TESTs with SITEBRICKS http client
  */
  @Test
  public void happyPathWithHttpClient() {
    ExpensesJson expensesJson = new ExpensesJson("water", "type", "23.31");

    WebResponse response = createInjector().getInstance(Web.class)
            .clientOf("http://localhost:8080/expenses")
            .transports(ExpensesJson.class).over(Json.class).post(expensesJson);

    assertTrue(response.toString().contains("Success"));
  }

  @Test
  public void addInvalidFundsWithHttpClient() {
    ExpensesJson expensesJson = new ExpensesJson("milk", "type", "invalid funds");

    WebResponse response = createInjector().getInstance(Web.class)
            .clientOf(getUrl() + "/expenses")
            .transports(ExpensesJson.class).over(Json.class).post(expensesJson);

    assertTrue(response.toString().contains("invalid funds"));
  }

  private Injector createInjector() {
    return Guice.createInjector(new AbstractModule() {
      protected void configure() {
        bind(ConverterRegistry.class).toInstance(new StandardTypeConverter(ImmutableSet.<Converter>of()));
      }
    });
  }
}