package io.github.jeqo.talk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class GreetingRepresentation {
  private final String greeting;

  @JsonCreator
  GreetingRepresentation(@JsonProperty("greeting") String greeting) {
    this.greeting = greeting;
  }

  @JsonProperty("greeting")
  public String greeting() {
    return greeting;
  }
}
