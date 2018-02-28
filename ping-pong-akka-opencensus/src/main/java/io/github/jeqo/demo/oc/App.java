package io.github.jeqo.demo.oc;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import io.opencensus.exporter.trace.zipkin.ZipkinTraceExporter;

import java.io.IOException;
import java.util.UUID;

/**
 *
 */
public class App {
  public static void main(String[] args) throws IOException {


    ZipkinTraceExporter.createAndRegister("http://docker-vm:9411/api/v2/spans", "ping-pong-oc");

    final ActorSystem actorSystem = ActorSystem.create("ping-pong-oc");
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
