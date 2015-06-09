package com.clouway.adapter.jdbc;

import com.clouway.adapter.rest.Expense;
import com.clouway.core.InvalidFundsCastException;
import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.api.client.util.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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
      new BigDecimal(funds);
    } catch (NumberFormatException r) {
      throw new InvalidFundsCastException();
    }

    Entity expensesEntity = new Entity("Expense");
    expensesEntity.setProperty("expense", funds);
    expensesEntity.setProperty("type", type);

    datastoreService.put(expensesEntity);
  }

  @Override
  public List<Expense> find(int numberOfItems, Integer pageNumber) {
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(numberOfItems);
    fetchOptions.offset(pageNumber);

    Query filteredQuery = new Query("Expense");
    PreparedQuery query = datastoreService.prepare(filteredQuery);
    QueryResultIterator<Entity> entities = query.asQueryResultIterator(fetchOptions);
    ArrayList<Expense> expenses = Lists.newArrayList();
    while (entities.hasNext()) {
      Entity entity = entities.next();
      expenses.add(new Expense(entity.getProperty("type").toString(), entity.getProperty("expense").toString()));
    }

    return expenses;
  }
}