package com.clouway;

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
  public void add(String id, BigDecimal funds) {
    Entity expensesEntity = new Entity("Expense", id);
    expensesEntity.setProperty("expense", funds.toString());

    datastoreService.put(expensesEntity);
  }

  @Override
  public Expense findOne(String id, BigDecimal bigDecimal) {
    Key key = KeyFactory.createKey("Expense", id);
    Entity fundEntity;
    BigDecimal currentFunds = null;
    try {
      fundEntity = datastoreService.get(key);
      currentFunds = new BigDecimal(fundEntity.getProperty("expense").toString());
    } catch (EntityNotFoundException e) {
      e.printStackTrace();
    }

    return new Expense(currentFunds);
  }
}