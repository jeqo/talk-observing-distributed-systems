package io.github.jeqo.demo.ot;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.IOException;
import java.util.UUID;

/**
 *
 */
public class App {

  public static void main(String[] args) throws IOException {

    final ActorSystem actorSystem = ActorSystem.create("ping-pong-ot");
    //// (1) Pong Service instantiation
    ActorRef pongServiceRef =
        actorSystem.actorOf(Props.create(PongService.class), "pong-service");
    //// (2) Ping Service instantiation
    ActorRef pingServiceRef =
        actorSystem.actorOf(Props.create(PingService.class, pongServiceRef), "ping-service");

    //// (3) Start an execution
    pingServiceRef.tell(new PingMessage(UUID.randomUUID().toString(), "jeqo"), ActorRef.noSender());

    try {
      System.out.println("Press ENTER to exit the system");
      System.in.read();
    } finally {
      actorSystem.terminate();
    }
  }

}
