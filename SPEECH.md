# Distributed Tracing with OpenTracing for Java Developers

## Where did my message go?

This presentation is about sharing my expirience about how did I 
became more eficient operating software that I shipped to 
production. 

I am a back-end and integration developer, that has been working
mostly with Oracle Middleware related technologies at the 
beginning of my professional life but today I have a full focus 
on open-source technologies.

One of the hardest things as an integration developer, that 
means that your tasks usually are related with connecting 
one source system with one or more sinks, is when once you
put things on production, the client comes and says: 
Where is this transaction? Of course, my platform has some
logging enabled (sometimes discourage by providers 
because it cause performance penalties on the platform)
of course I can go to the database and check if that row has 
been correctly updated. But once you start figuring out 
for each scenarios, for each technology you connect from/to
this work starts to becoming more and more tedious, but it is
still important to do. 

So, yes, I enable monitoring and tracing on my integration platform,
I can now have some metrics about what is going on in a particular
pipeline, but what happens when I move outside my platform? 
What happen when I have another integration platform to communicate 
with? It is a similar scenario like "it work in my computer" 
but this time is "it works in my platform". 

Propietary platforms like Oracle have great tools to deal with it
but are only available for some products, and extensibility is 
not an option always.

So, what to do? 

I have been asking me this for some time until I start following
some cool projects like Kubernetes and Prometheus that are part
of a foundation called CNCF: The Cloud Native Computing Foundation
and one of the projects is called OpenTracing. 

OpenTracing comes as an effort to solve the same problem but 
probably from different perspective but similar needs.
Microservices was the hottest topic a couple of years ago, and
now we have talks about "please don't do microservices if you are 
not this big"

One of the biggest challenges in microservices is that instead of 
having one monolithic component to manage, you spread this 
into a bunch of services that at least suppose to be autonomous. 
This autonomy sounds great until you put things into production, 
and your client comes and say, did this transaction has been 
processed correctly? Yeap, a very similar problem that the one 
that I have. 

So, Distributed Tracing is this ability to have visibility
over the execution of your transactions on a set of distributed 
components. Basically you will have a tool to tell you what happened
with your request, which systems has been hitten by your request, 
where is it know and how long is taking in each stage. 

Cool right? So, today we will have a full live demo to show how 
to instrument a monolithic and a distributed application with 
OpenTracing.

## Use Case: Bug Tracker

Just to keep things simple and actually focus on instrumentation
I have choose a simple use case: Report and visualize bugs.

This application has a couple of versions.

A monolithic one, where we have a web-app (please, remember I'm 
a back-end developer :)), and one service, and a database.

And also a cooler approach using CQRS and distributed services
to achieve basically the same but feelling a greater benefit 
from distributed tracing.

## Monolith

This monolith is implemented with the following technologies:

* Play Framework on the front end side. 
* Dropwizard to implement my service and,
* Postgres Database to store data.

I won't describe much of these technologies and frameworks, 
but you usually can replace it with what you use and the result 
should be very similar.

//TODO run and functional test the application

//TODO explain what is happening for each request

## Tracing platform: Jaeger

//TODO deploy jaeger

//TODO create your first trace

## Instrument your monolith

//TODO Instrument incoming requests

//TODO Instrument back-end calls

//TODO create your own spans

## Distributed applications

Monolith is cool but today we are all doing microservices right?

We will use a pattern called CQRS that tells you to divide your
reading and writing model, so what you receive as request: commands
should be processed with one model, and generate events, that will 
be consumed by some kind of indexer that will materialize a view
prepared to be queried from your application.

In the case of a bug trucker, what we receive from the UI as a 
request to create a bug is not what we offer then as a query, 
so we usually denormalize information to make queries more efficient.

This pattern is been used by companies as Capital One in the US 
and frameworks as Lagom from Lightbend. Actually this demo is based on 
Capital One's presentation about CQRS.

So, we will split our back-end on 4 different components:

1. Commander: This module will expose an HTTP interface for creating
commands.

2. Service: This module will apply business logic on commands and 
produce events. 

3. Indexer: This module will consume events and create a materialized

4. Query: This module will offer an HTTP interface to query your views.

Make sense so far?

A couple of technologies will be added: 

Apache Kafka: will be our log where commands and events will be stored. 

Elasticsearch: will be our search index.

Fair enought.

So let's do it? 

//TODO show a version of this version working

//TODO instrument back-ends

//TODO add your spans

//TODO make sense of your application

To finish I want to point that Tracing is just one part of your application
performance monitoring (AKA APM), you need to complement this with another 
tools for Logging and Metrics that will give you the right tools to be able 
to really operate in a decent way your systems.

Hope you find this presentation useful and thanks a lot for your time!
