package com.clouway.adapter.rest;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ResponseMessage {
  private String message;

  public ResponseMessage() {

  }

  public ResponseMessage withMessage(String message) {
    this.message = message;
    return this;
  }

  public String getMessage() {
    return message;
  }
}
