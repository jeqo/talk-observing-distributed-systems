package io.github.jeqo.demo;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

/**
 *
 */
public class OtroProceso {

  private final Tracer tracer ;

  public OtroProceso(Tracer tracer) {
    this.tracer = tracer;
  }

  public void run() {
    ActiveSpan span = tracer.buildSpan("main").startActive();
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
