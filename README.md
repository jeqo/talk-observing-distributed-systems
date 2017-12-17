# Talk: Where did my message goes? Distributed Tracing with OpenTracing

Presented at:

* [NoSlidesConf 2017](http://www.noslidesconf.net/)

* [PeruJUG Meetup](https://www.meetup.com/es-ES/Peru-Java-User-Group/events/245246354/) [[Slides](https://speakerdeck.com/jeqo/observando-sistemas-distribuidos)]

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

* Benjamin Sigelman et al. - “Dapper, a Large-Scale Distributed Systems Tracing Infrastructure” https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/36356.pdf  

* Raja R. Sambasivan et al. “So, you want to trace your distributed system? Key design insights from years of practical experience”
http://www.pdl.cmu.edu/PDL-FTP/SelfStar/CMU-PDL-14-102.pdf 

### Blog posts:

* https://medium.com/@copyconstruct/monitoring-in-the-time-of-cloud-native-c87c7a5bfa3e 
* https://peter.bourgon.org/ok-log/ 
* https://peter.bourgon.org/blog/2017/02/21/metrics-tracing-and-logging.html 
* https://medium.com/opentracing/take-opentracing-for-a-hotrod-ride-f6e3141f7941 
* https://eng.uber.com/distributed-tracing/
* https://medium.com/jaegertracing/jaeger-and-multitenancy-99dfa1d49dc0
* https://medium.com/@copyconstruct/monitoring-and-observability-8417d1952e1c
* https://codeascraft.com/2011/02/15/measure-anything-measure-everything/
* https://medium.com/@copyconstruct/the-death-of-ops-is-greatly-exaggerated-ff3bd4a67f24
* https://medium.com/@copyconstruct/logs-and-metrics-6d34d3026e38
* https://12factor.net/logs
* https://medium.com/opentracing/take-opentracing-for-a-hotrod-ride-f6e3141f7941
* https://blog.codinghorror.com/the-problem-with-logging/
* https://peter.bourgon.org/blog/2016/02/07/logging-v-instrumentation.html 

### Books:

* Martin Kleppmann - “Design Data-Intensive Applications” https://dataintensive.net/

* Google - "Site Reliability Engineering” https://landing.google.com/sre/book/index.html  

