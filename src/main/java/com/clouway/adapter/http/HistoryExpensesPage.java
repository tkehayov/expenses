package com.clouway.adapter.http;

import com.clouway.core.ExpensesRepository;
import com.clouway.adapter.rest.Expense;
import com.clouway.core.InvalidPageNumberException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;

import java.util.List;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@At("/expenses/page/:pageNumber/size/:pageSize")
@Service
public class HistoryExpensesPage {
  private final ExpensesRepository repository;

  @Inject
  public HistoryExpensesPage(Provider<ExpensesRepository> repository) {
    this.repository = repository.get();
  }

  @Get
  public Reply<?> getItems(@Named("pageNumber") Integer pageNumber, @Named("pageSize") Integer pageSize) {

    List<Expense> expenses;
    try {
      expenses = repository.find(pageSize, pageNumber);
    } catch (InvalidPageNumberException e) {
      return Reply.saying().error();
    }

    return Reply.with(expenses).as(Json.class);
  }
}