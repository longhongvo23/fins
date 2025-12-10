# JHipster generated Docker-Compose configuration

## Usage

Launch all your infrastructure by running: `docker compose up -d`.

## Configured Docker services

### Service registry and configuration server:

- [Consul](http://localhost:8500)

### Applications and dependencies:

- gateway (gateway application)
- gateway's mongodb database
- userservice (microservice application)
- userservice's mongodb database
- notificationservice (microservice application)
- notificationservice's mongodb database
- stockservice (microservice application)
- stockservice's mongodb database
- newsservice (microservice application)
- newsservice's mongodb database
- crawlservice (microservice application)
- crawlservice's mongodb database

### Additional Services:

- Kafka
- [Prometheus server](http://localhost:9090)
- [Prometheus Alertmanager](http://localhost:9093)
- [Grafana](http://localhost:3000)
