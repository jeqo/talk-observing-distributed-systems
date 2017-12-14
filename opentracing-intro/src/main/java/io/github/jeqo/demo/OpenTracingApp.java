package io.github.jeqo.demo;

import io.opentracing.Tracer;

import java.util.Map;

import static java.lang.System.out;

/**
 *
 */
public class OpenTracingApp {

  public static void main(String[] args) throws InterruptedException {
    final Tracer zipkinTracer = TracerBuilder.getZipkinTracer("Process");
    final Process process = new Process(zipkinTracer);
    final Map<String, String> map = process.run();

    out.println(map);

    final Tracer anotherZipkinTracer = TracerBuilder.getZipkinTracer("AnotherProcess");
    final AnotherProcess anotherProcess = new AnotherProcess(anotherZipkinTracer);
    anotherProcess.run(map);

    Thread.sleep(2000);
    out.println("Shouting down ...");
  }

}
