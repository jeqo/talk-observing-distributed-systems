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
    ActorRef pongServiceRef = actorSystem.actorOf(Props.create(PongService.class), "pong-service");
    ActorRef pingServiceRef = actorSystem.actorOf(Props.create(PingService.class, pongServiceRef), "ping-service");

    pingServiceRef.tell(new PingMessage(UUID.randomUUID().toString(), "jeqo"), ActorRef.noSender());

    try {
      System.out.println("Press ENTER to exit the system");
      System.in.read();
    } finally {
      actorSystem.terminate();
    }
  }

}
