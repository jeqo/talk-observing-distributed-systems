package io.github.jeqo.demo;

import java.util.Map;

import static java.lang.System.out;

/**
 *
 */
public class OpenTracingApp {

  public static void main(String[] args) throws InterruptedException {
    Process process = new Process(TracerBuilder.getJaegerTracer("Process"));
    Map<String, String> map = process.run();

    AnotherProcess anotherProcess = new AnotherProcess(TracerBuilder.getJaegerTracer("AnotherProcess"));
    anotherProcess.process(map);

    Thread.sleep(2000);
    out.println("Shouting down ...");
  }

}
