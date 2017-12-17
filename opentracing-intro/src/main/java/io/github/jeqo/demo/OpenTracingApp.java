package io.github.jeqo.demo;

import io.opentracing.Tracer;

import java.util.Map;

import static java.lang.System.out;

/**
 *
 */
public class OpenTracingApp {

  public static void main(String[] args) throws InterruptedException {
    final Tracer tracer = TracerBuilder.getJaegerTracer("Process");
    final Process process = new Process(tracer);
    final Map<String, String> map = process.run();

    out.println(map);

    final Tracer anotherTracer = TracerBuilder.getJaegerTracer("AnotherProcess");
    final AnotherProcess anotherProcess = new AnotherProcess(anotherTracer);
    anotherProcess.run(map);

    Thread.sleep(2000);
    out.println("Shouting down ...");
  }

}
