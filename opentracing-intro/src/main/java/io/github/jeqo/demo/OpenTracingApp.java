package io.github.jeqo.demo;

import io.opentracing.Tracer;

import static java.lang.System.out;

/**
 *
 */
public class OpenTracingApp {

  public static void main(String[] args) throws InterruptedException {


    Tracer tracer = TracerBuilder.getZipkinTracer("Proceso");
    Proceso proceso = new Proceso(tracer);
    proceso.run();


    Tracer otroTracer = TracerBuilder.getZipkinTracer("Proceso");
    OtroProceso otroProceso = new OtroProceso(otroTracer);
    otroProceso.run();

    Thread.sleep(2000);
    out.println("Shutting down ...");
  }

}
