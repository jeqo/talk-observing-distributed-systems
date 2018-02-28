package io.github.jeqo.demo.ot;

import java.io.Serializable;
import java.util.Map;

/**
 *
 */
public class PongMessage implements Serializable {
  private final Map<String, String> context;

  PongMessage(Map<String, String> context) {
    this.context = context;
  }

  public Map<String, String> getContext() {
    return context;
  }
}
