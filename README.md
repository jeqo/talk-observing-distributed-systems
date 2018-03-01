# Talk: Where did my message goes? Distributed Tracing with OpenTracing

Presented at:

* [NoSlidesConf 2017](http://www.noslidesconf.net/)

* [PeruJUG Meetup](https://www.meetup.com/es-ES/Peru-Java-User-Group/events/245246354/) [[Slides](https://speakerdeck.com/jeqo/observando-sistemas-distribuidos-perujug)]

* [GDG Oslo Meetup](https://www.meetup.com/es-ES/GDG-Cloud-Norway/events/247282228) [[Slides](https://speakerdeck.com/jeqo/increasing-observability-with-distributed-tracing)]

**Observability** is the ability to understand what is going on with your systems, not 
only from the point of view of how the system looks from outside, but been able to
answer more granular questions, like where did this message goes?

`Metrics`, `Logging` and `Tracing` are called the three pillars of Observability.

In this presentation we will see how we can use these tools and how they are related 
to be able to observe our systems.

* Logging and Metrics

* OpenTracing API

* Demo: Tweets App

## Tools

* JDK 8
* Docker (Docker-Machine, host: docker-vm)

* Logging: Fluentd, Elasticsearch Kibana
* Metrics: Prometheus
* Tracing: OpenTracing, Jaeger, Zipkin

* Frameworks/Libraries: Dropwizard, JOOQ, Kafka Clients, HTTP Client, Elasticsearch, Postgresql.

## Key takeaways

* Distributed Tracing is just one more tool for your toolkit, and is not mean to replace metrics 
and logging, and it could be seen as an abstraction of them.

* OpenTracing is an effort to standarize how to instrument your applications, so you can upgrade/migrate
your infrastructure without changing your implementations.

* OpenTracing is a young project, go ahead, try out and give feedback to the community, or contribute
to make it better.

## Resources

### Papers: 

* **dapper** https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/36356.pdf 
* **canopy** http://cs.brown.edu/~jcmace/papers/kaldor2017canopy.pdf
* **automating failure testing research at internet scale** https://people.ucsc.edu/~palvaro/socc16.pdf 
* data on the outside vs data on the inside http://cidrdb.org/cidr2005/papers/P12.pdf 
* pivot tracing http://sigops.org/sosp/sosp15/current/2015-Monterey/printable/122-mace.pdf 

### Blog posts:

* ok log https://peter.bourgon.org/ok-log/ 
* logs - 12 factor application https://12factor.net/logs 
* the problem with logging https://blog.codinghorror.com/the-problem-with-logging/ 
* logging v. instrumentation https://peter.bourgon.org/blog/2016/02/07/logging-v-instrumentation.html 
* logs and metrics https://medium.com/@copyconstruct/logs-and-metrics-6d34d3026e38 
* measure anything, measure everything https://codeascraft.com/2011/02/15/measure-anything-measure-everything/ 
* metrics, tracing and logging https://peter.bourgon.org/blog/2017/02/21/metrics-tracing-and-logging.html 
* monitoring and observability https://medium.com/@copyconstruct/monitoring-and-observability-8417d1952e1c 
* monitoring in the time of cloud native https://medium.com/@copyconstruct/monitoring-in-the-time-of-cloud-native-c87c7a5bfa3e 
* sre book https://landing.google.com/sre/book/index.html 
* distributed tracing at uber https://eng.uber.com/distributed-tracing/ 
* spigo and simianviz https://github.com/adrianco/spigo 
* observability: what’s in a name? https://honeycomb.io/blog/2017/08/observability-whats-in-a-name/ 
* wtf is operations? #serverless https://charity.wtf/2016/05/31/wtf-is-operations-serverless/ 
* event foo: what should i add to an event https://honeycomb.io/blog/2017/08/event-foo-what-should-i-add-to-an-event/ 
* “The Verification of A Distributed System” - Caitie McCaffrie https://github.com/CaitieM20/Talks/tree/master/TheVerificationOfADistributedSystem 
* “Testing in Production” by Charity Majors https://opensource.com/article/17/8/testing-production 
* “Data on the outside vs Data on the inside - Review” by Adrian Colyer https://blog.acolyer.org/2016/09/13/data-on-the-outside-versus-data-on-the-inside/ 
* Google’s approach to Observability https://medium.com/@rakyll/googles-approach-to-observability-frameworks-c89fc1f0e058 
* Microservices and Observability https://medium.com/@rakyll/microservices-observability-26a8b7056bb4 
* Best Practices for Observability https://honeycomb.io/blog/2017/11/best-practices-for-observability/ 
* https://thenewstack.io/dev-ops-doesnt-matter-need-observability/ 

### talks

* "Observability for Emerging Infra: What Got You Here Won't Get You There" by Charity Majors https://www.youtube.com/watch?v=1wjovFSCGhE  
* “The Verification of a Distributed System” by Caitie McCaffrey https://www.youtube.com/watch?v=kDh5BrqiGhI 
* “Mastering Chaos - A Netflix Guide to Microservices” by Josh Evans https://www.youtube.com/watch?v=CZ3wIuvmHeM
* “Monitoring Microservices” by Tom Wilkie https://www.youtube.com/watch?v=emaPPg_zxb4
* “Microservice application tracing standards and simulations” by Adrian Cole and Adrian Cockcroft https://www.slideshare.net/adriancockcroft/microservices-application-tracing-standards-and-simulators-adrians-at-oscon 
* “Intuition Engineering at Netflix” by Justin Reynolds https://vimeo.com/173607639 
* Distributed Tracing: Understanding how your all your components work together by José Carlos Chávez https://speakerdeck.com/jcchavezs/distributed-tracing-understanding-how-your-all-your-components-work-together 
* “Monitoring isn't just an accident” https://docs.google.com/presentation/d/1IEJIaQoCjzBsVq0h2Y7qcsWRWPS5lYt9CS2Jl25eurc/edit#slide=id.g327c9fd948_0_534 
* Orchestrating Chaos Applying Database Research in the Wild - Peter Alvaro https://www.youtube.com/watch?v=YplkQu6a80Q 

### Books:

* Martin Kleppmann - “Design Data-Intensive Applications” https://dataintensive.net/

* Google - "Site Reliability Engineering” https://landing.google.com/sre/book/index.html  

