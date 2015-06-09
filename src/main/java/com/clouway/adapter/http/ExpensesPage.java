package com.clouway.adapter.http;

import com.clouway.adapter.rest.Expense;
import com.clouway.adapter.jdbc.ExpensesRepository;
import com.clouway.core.InvalidFundsCastException;
import com.clouway.adapter.rest.ResponseMessage;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Post;

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
  public Reply<ResponseMessage> add(Request request) {
    Expense expenses = request.read(Expense.class).as(Json.class);
    ResponseMessage responseMessage = new ResponseMessage().withMessage("Success");
    try {
      repository.add(expenses.getType(), expenses.getExpenses());
    } catch (InvalidFundsCastException fe) {
      responseMessage.withMessage("invalid funds");
    }

    return Reply.with(responseMessage).as(Json.class);
  }
}
