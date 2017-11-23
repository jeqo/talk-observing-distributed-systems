package io.github.jeqo.demo;

import static java.lang.System.out;

/**
 *
 */
public class OpenTracingApp {

  public static void main(String[] args) throws InterruptedException {
    Process process = new Process();
    process.run();

    Thread.sleep(2000);
    out.println("Shouting down ...");
  }

}
