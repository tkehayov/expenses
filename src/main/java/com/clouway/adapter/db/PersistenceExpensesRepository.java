package com.clouway.adapter.db;

import com.clouway.adapter.rest.Expense;
import com.clouway.core.ExpensesRepository;
import com.clouway.core.InvalidFundsCastException;
import com.clouway.core.InvalidPageNumberException;
import com.clouway.core.NegativeFundsException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.com.google.api.client.util.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class PersistenceExpensesRepository implements ExpensesRepository {
  private final DatastoreService datastoreService;

  @Inject
  public PersistenceExpensesRepository(Provider<DatastoreService> service) {
    this.datastoreService = service.get();
  }

  @Override
  public void add(String type, String funds) {
    try {
      checkForNegativeFunds(funds);
    } catch (NumberFormatException r) {
      throw new InvalidFundsCastException();
    }

    Entity expensesEntity = new Entity("Expense");
    expensesEntity.setProperty("expense", funds);
    expensesEntity.setProperty("type", type);

    datastoreService.put(expensesEntity);
  }

  @Override
  public List<Expense> find(int pageSize, Integer pageNumber) {
    QueryResultIterator<Entity> entities = getPageElements(pageSize, pageNumber);

    return getExpenses(entities);
  }

  private QueryResultIterator<Entity> getPageElements(int pageSize, Integer pageNumber) {
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);

    try {
      fetchOptions.offset(pageNumber - 1);
    } catch (IllegalArgumentException e) {
      throw new InvalidPageNumberException();
    }

    Query filteredQuery = new Query("Expense");
    PreparedQuery query = datastoreService.prepare(filteredQuery);

    return query.asQueryResultIterator(fetchOptions);
  }

  private List<Expense> getExpenses(QueryResultIterator<Entity> entities) {
    List<Expense> expenses = Lists.newArrayList();

    while (entities.hasNext()) {
      Entity entity = entities.next();
      expenses.add(new Expense(entity.getProperty("type").toString(), entity.getProperty("expense").toString()));
    }

    return expenses;
  }

  private boolean isNegative(String funds) {
    return new BigDecimal(funds).signum() == -1;
  }

  private void checkForNegativeFunds(String funds) {
    if (isNegative(funds)) {
      throw new NegativeFundsException();
    }
  }
}