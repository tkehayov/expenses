package com.clouway;
/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.sitebricks.SitebricksModule;

public class AppConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new SitebricksModule() {
      @Override
      protected void configureSitebricks() {
        scan(AppConfig.class.getPackage());
      }
    }, new AbstractModule() {
      @Override
      protected void configure() {
        bind(ExpensesJson.class).annotatedWith(Names.named("expensesJsonAnnotation")).to(ExpensesJson.class);
      }

      @Provides
      public DatastoreService getDataStore() {
        return DatastoreServiceFactory.getDatastoreService();
      }

      @Provides
      public ExpensesRepository getExpenseRepository(Provider<DatastoreService> service) {
        return new PersistenceExpensesRepository(service);
      }
    });

  }
}