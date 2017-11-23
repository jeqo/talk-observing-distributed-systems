package io.github.jeqo.demo;

/**
 *
 */
class Process {

  void run() {

  }

  private void waitABit() {
    try {
      Thread.sleep((long) (Math.random() * 1000));
    }catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}