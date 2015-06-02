package com.clouway;

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

import java.math.BigDecimal;

import static com.google.inject.util.Providers.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ExpensesTest {
  private final LocalServiceTestHelper dataStore = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    dataStore.setUp();
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

  @Test
  public void happyPath() {
    DatastoreService service = DatastoreServiceFactory.getDatastoreService();
    BigDecimal funds = new BigDecimal("44444");
    final ExpensesJson json = new ExpensesJson(funds.toString());
    ExpensesRepository repository = new PersistenceExpensesRepository(of(service));
    ExpensesPage page = new ExpensesPage(of(repository));

    context.checking(new Expectations() {{
      oneOf(request).read(ExpensesJson.class);
      will(returnValue(requestRead));
      oneOf(requestRead).as(Json.class);
      will(returnValue(json));
    }});
    page.add(request);
    Expense one = repository.findOne(funds);

    assertThat(one.getFunds(), is(funds));
  }
}