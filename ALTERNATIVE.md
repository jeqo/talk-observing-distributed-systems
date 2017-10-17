# Alternative workflow

Start explaining OpenTracing from scratch. This means that the
use-case is not the main part of the presentation but the closing
part.

This talk will have a couple of co-lateral topics. So even if the
attendees have experience with open-tracing they can take something
from this talk.


## Colateral Topics

* Apache Kafka
* Event Sourcing and CQRS

Why these topics? Just for fun and to enter into a use-case where
distributed tracing can be more appreciated it.

## Steps

### Logging-based tracking (15 min)

#### Simple HTTP client and HTTP server. (5 min)

1. Test an already implemented client/server

2. Add logging, everything looks good.

#### Simple HTTP client, distributed HTTP servers (2 min)

1. Test implementation, and routing

2. Add logging, a bit more work, but ok enough.

#### Centralized logging (3 min)

1. Add Elastic Stack to bring logs together.

2. Test

3. Analyze logs, much better

#### Move to messaging and asynchronous processing (5 min)

1. Test and already implemented producer, and two consumers (1 fast/1 slow)

2. Logs are a key part of your APM platform, why do we need tracing?

2 consumers is a simple scenario: The history is not always one way.
Events propagate around your systems, making sense of correlation and
causality is not as obvious as with 2 or three components.

### Add Distributed Tracing (10 min)

#### Simple HTTP client/server (3 min)

1. Add OpenTracing to the client side

2. Add OpenTracing to the server side

3. Start Jaeger

4. Test

5. Visualize traces

#### Simple HTTP client, distributed HTTP servers (2 min)

1. Add OpenTracing to the server side

2. Test

3. Visualize traces

#### Add Tracing to Kafka deployment (5 min)

1. Using integration with Kafka and OpenTracing

### Additional use-cases (15 min)

1. What about metadata? tags, baggage items.

2. What about errors?

3. What about analytics?

4. What about production ready deployment?


---

* Simple client request can be deployed with Docker, so first and second
use-case are based on the same code base.

* Kafka-based demo should be in another directory keeping the same
structure

* Real-life demo should be in another directory to show how does it
look if you use every Jaeger component.