package io.github.jeqo.demo;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

/**
 *
 */
public class Proceso {

  private final Tracer tracer;

  public Proceso(Tracer tracer) {
    this.tracer = tracer;
  }

  public void run () {
    ActiveSpan span = tracer.buildSpan("main").startActive();

    span.setTag("transactionId", "1");
    childOperation();
    waitABit();
    span.close();
  }

  private void childOperation() {
    ActiveSpan span = tracer.buildSpan("child").startActive();

    waitABit();
    span.close();
  }

  private void waitABit() {
    try {
      Thread.sleep((long) (Math.random() * 1000));
    }catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
