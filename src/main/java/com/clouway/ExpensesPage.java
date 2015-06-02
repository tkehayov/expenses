package com.clouway;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;

import java.math.BigDecimal;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
@At("/expenses")
@Service
public class ExpensesPage {
  private final ExpensesRepository repository;

  @Inject
  public ExpensesPage(Provider<ExpensesRepository> repository) {
    this.repository = repository.get();
  }

  @Post
  public Reply<ExpensesJson> add(Request request) {
    ExpensesJson expensesJson = request.read(ExpensesJson.class).as(Json.class);
    repository.add(new BigDecimal(expensesJson.getExpenses()));

    return Reply.with(expensesJson)
            .as(Json.class);
  }
}
