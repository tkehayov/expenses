package com.clouway.adapter.jdbc;

import com.clouway.core.Expense;
import com.clouway.core.InvalidFundsCastException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.math.BigDecimal;

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
  public void add(String id, String type, String funds) {
    try {
      new BigDecimal(funds);
    } catch (NumberFormatException r) {
      throw new InvalidFundsCastException();
    }

    Entity expensesEntity = new Entity("Expense", id);
    expensesEntity.setProperty("expense", funds);
    expensesEntity.setProperty("type", type);

    datastoreService.put(expensesEntity);
  }

  @Override
  public Expense findOne(String id, String bigDecimal) {
    Key key = KeyFactory.createKey("Expense", id);
    Entity fundEntity;
    String currentFunds = null;
    try {
      fundEntity = datastoreService.get(key);
      currentFunds = fundEntity.getProperty("expense").toString();
    } catch (EntityNotFoundException e) {

    }

    return new Expense(currentFunds.toString());
  }
}