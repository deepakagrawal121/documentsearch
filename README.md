# Distributed Document Search Service

A simplified prototype of a multi-tenant distributed document search service built using **Java 17**, **Spring Boot**, **PostgreSQL**, **Elasticsearch**, and **Redis**.

The system demonstrates core enterprise patterns including:

- REST-based document ingestion and search  
- Multi-tenant isolation  
- Full-text search with Elasticsearch  
- Query caching with Redis  
- Basic per-tenant rate limiting  
- Health checks with dependency status  

This prototype focuses on architectural clarity over production-level completeness.

---

## Architecture Overview

### Components

- **Spring Boot API** – Document management and search APIs  
- **PostgreSQL** – System of record for documents  
- **Elasticsearch** – Full-text search engine  
- **Redis** – Cache for hot search queries  
- **Bucket4j** – In-memory rate limiting per tenant  
- **Docker Compose** – Local orchestration  

### Key Characteristics

- Header-based multi-tenancy (`X-Tenant-Id`)
- Elasticsearch routing using `tenantId`
- Redis-based search caching
- Stateless API layer for horizontal scaling

---

## Tech Stack

- Java 17  
- Spring Boot 3  
- PostgreSQL  
- Elasticsearch 8  
- Redis  
- Maven  
- Docker  

---

## Running Locally

### Prerequisites

- Java 17
- Maven
- Docker / Docker Compose

---

### Start Infrastructure

```bash
docker-compose up -d
```

Starts:

- PostgreSQL → localhost:5432  
- Elasticsearch → localhost:9200  
- Redis → localhost:6379  

---

### Run Application

```bash
mvn spring-boot:run
```

Application runs on:

```
http://localhost:8080
```

---

## API Endpoints

### Create Document

```bash
curl -X POST http://localhost:8080/documents \
-H "Content-Type: application/json" \
-H "X-Tenant-Id: tenant1" \
-d '{
  "title":"Distributed Systems",
  "content":"Search at scale with Elasticsearch"
}'
```

---

### Search Documents

```bash
curl --location 'http://localhost:8080/search?q=scale&tenant=tenant1' \
--header 'X-Tenant-Id: tenant1'
```

---

### Get Document

```bash
curl http://localhost:8080/documents/{id} \
-H "X-Tenant-Id: tenant1"
```

---

### Delete Document

```bash
curl -X DELETE http://localhost:8080/documents/{id}
```

---

## Multi-Tenancy

- Tenant identified via `X-Tenant-Id`
- Documents stored with tenantId
- Elasticsearch queries filtered by tenant
- Redis cache keys prefixed by tenant

---

## Rate Limiting

- Bucket4j based
- Default: 100 requests/minute/tenant

---

## Caching

- Redis caches search results for 60 seconds

---

## Health Check

```bash
curl http://localhost:8080/actuator/health
```

Shows PostgreSQL, Redis and Elasticsearch status.

---

## Consistency Model

- PostgreSQL = strong consistency
- Elasticsearch = eventual consistency

---

## Production Enhancements (Not Implemented)

- Kafka async indexing
- JWT auth
- Distributed rate limiting
- Circuit breakers
- Prometheus metrics
- OpenTelemetry tracing
- Blue-green deployments

---

## AI Tool Usage

ChatGPT assisted with architecture, prototype scaffolding, and documentation.

---

## Author

Deepak Agrawal  
Backend Software Engineer – Distributed Systems
