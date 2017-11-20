package io.github.jeqo.demo.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AckRepresentation {
  private boolean result;
  private String message;

  public AckRepresentation() {
    result = true;
  }

  public AckRepresentation(String message) {
    result = false;
    this.message = message;
  }

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
