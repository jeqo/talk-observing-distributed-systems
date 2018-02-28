package io.github.jeqo.demo.oc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class PingMessage implements Serializable {
  private final Map<String, String> context;
  private final String transactionId;
  private final String userId;

  PingMessage(String transactionId, String userId) {
    this.userId = userId;
    this.context = new HashMap<>();
    this.transactionId = transactionId;
  }

  public Map<String, String> getContext() {
    return context;
  }

  public String getTransactionId() {
    return transactionId;
  }

  String getUserId() {
    return userId;
  }
}
