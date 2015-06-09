package com.clouway.adapter.http;

import com.clouway.adapter.jdbc.ExpensesRepository;
import com.clouway.adapter.rest.Expense;
import com.clouway.adapter.rest.PageItems;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

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
  public Reply<List<Expense>> getItems(@Named("pageNumber") String pageNumber, @Named("pageSize") Integer pageSize) {
    List<Expense> expenses = repository.find(pageSize, Integer.parseInt(pageNumber));

    return Reply.with(expenses).as(Json.class);
  }
}