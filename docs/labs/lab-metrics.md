### Instrumentation Libraries

```xml
    <dependencies>
        <dependency>
            <groupId>io.dropwizard</groupId>
            <artifactId>dropwizard-core</artifactId>
        </dependency>
        
        <!-- Metrics Instrumentation Prometheus -->
        <dependency>
            <groupId>io.prometheus</groupId>
            <artifactId>simpleclient_dropwizard</artifactId>
        </dependency>
        <dependency>
            <groupId>io.prometheus</groupId>
            <artifactId>simpleclient_servlet</artifactId>
        </dependency>
    </dependencies>
```

### Add Instrumentation to the Application

```java
public class HelloWorldMonolithApp extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new HelloWorldMonolithApp().run(args);
  }

  @Override
  public void run(Configuration configuration, Environment environment) throws Exception {
    // Preparing Translation Service
    final TranslationService translationService = new TranslationService();
    // Preparing Greeting Service and inject Translation
    final GreetingResource greetingService = new GreetingResource(translationService);

    // Register Greeting Service
    environment.jersey().register(greetingService);

    // Add Metrics Instrumentation to count requests
    final CollectorRegistry collectorRegistry = new CollectorRegistry();
    collectorRegistry.register(new DropwizardExports(environment.metrics()));

    // Register Metrics Servlet
    environment.admin()
        .addServlet("metrics", new MetricsServlet(collectorRegistry))
        .addMapping("/metrics");
  }
}
```

### Instrument your Endpoint

```java
@Path("hello")
public class GreetingResource {

  private final TranslationService translationService;

  public GreetingResource(TranslationService translationService) {
    this.translationService = translationService;
  }

  @GET
  @Path("{name}")
  @Timed //Metrics Instrumentation
  public Response hello(@PathParam("name") String name,
                        @QueryParam("lang") String lang) {
    final String hello = translationService.translate(lang);
    final String entity = hello + " " + name;
    return Response.ok(entity).build();
  }
}
```

### Evaluate Load with Prometheus

1. Start Load Test from SoapUI

2. Evaluate Rate with Prometheus

```
rate(io_github_jeqo_demo_rest_GreetingResource_hello_count[20s])
```