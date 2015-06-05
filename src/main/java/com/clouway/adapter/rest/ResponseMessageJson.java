package com.clouway.adapter.rest;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ResponseMessageJson {
  private String message;

  public ResponseMessageJson() {

  }

  public ResponseMessageJson withMessage(String message) {
    this.message = message;
    return this;
  }

  public String getMessage() {
    return message;
  }
}
