### Check Logs on Monolith

### Check Logs on Distributed version

### Scale Service

### Check Logs on Distributed version

### Aggregate Logs with Fluent

1. Start `apm.logging.yml` docker compose

2. Start distributed +  logging

```yaml
version: '3'
services:
  hello-world-client:
    build: hello-world-distributed/client
    ports:
      - 8090:8080
    logging:
      driver: "fluentd"
      options:
        fluentd-address: localhost:24224
  hello-world-service:
    build: hello-world-distributed/service
    ports:
      - 8080
    logging:
      driver: "fluentd"
      options:
        fluentd-address: localhost:24224
  hello-translation-service:
    build: hello-world-distributed/translation
    ports:
      - 8080
    logging:
      driver: "fluentd"
      options:
        fluentd-address: localhost:24224
```