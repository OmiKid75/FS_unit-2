# Campus Maintenance API

A complete Spring Boot project designed to demonstrate Experiments 2.1.1 and 2.1.2: RESTful API design, CRUD, validation, standardized responses, CORS, global exception handling, logging, and correlation IDs.

## Requirements
- Java 17+
- Maven 3.9+
- Postman/Insomnia/curl

## Run
```bash
mvn spring-boot:run
```
API: `http://localhost:8080`

Health: `GET /api/health`
H2 console: `http://localhost:8080/h2-console`
JDBC URL: `jdbc:h2:mem:campusdb` | user: `sa` | password: blank

## Endpoints
- `GET /api/requests`
- `GET /api/requests/{id}`
- `POST /api/requests`
- `PUT /api/requests/{id}`
- `PATCH /api/requests/{id}/status?status=RESOLVED`
- `DELETE /api/requests/{id}`

## Sample POST
```json
{
  "title": "Broken study-room light",
  "description": "The main light in the study room is not working.",
  "category": "ELECTRICAL",
  "priority": "HIGH",
  "studentName": "Naomi",
  "room": "D-102"
}
```

Categories: `ELECTRICAL`, `PLUMBING`, `INTERNET`, `FURNITURE`, `CLEANING`, `OTHER`

Priorities: `LOW`, `MEDIUM`, `HIGH`, `URGENT`

Statuses: `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CANCELLED`

## Experiment 2.1.1
- REST controller and resource-based URIs
- CRUD operations
- Controller → Service → Repository architecture
- DTO + Jakarta Bean Validation
- Standard `ApiResponse<T>` JSON structure
- Appropriate HTTP status codes
- Restricted CORS for localhost frontends
- Spring Data JPA + H2 persistence

## Experiment 2.1.2
- Centralized `@RestControllerAdvice`
- `ResourceNotFoundException`
- Validation and malformed-request handling
- SLF4J/Logback logging
- `CorrelationIdFilter`
- `X-Correlation-ID` response header
- MDC correlation ID in console logs
- Request status and execution-time logging

## Standard response
```json
{
  "success": true,
  "message": "Maintenance request retrieved successfully",
  "data": {},
  "timestamp": "2026-01-20T10:31:00",
  "path": "/api/requests/1",
  "correlationId": "..."
}
```

## Demo sequence
1. `GET /api/health`
2. `GET /api/requests`
3. `POST /api/requests`
4. Send invalid POST to demonstrate validation
5. `PUT /api/requests/1`
6. `PATCH /api/requests/1/status?status=RESOLVED`
7. Request `/api/requests/9999` to demonstrate 404 handling
8. `DELETE /api/requests/1`
9. Show terminal logs and the `X-Correlation-ID`

## Project structure
```text
src/main/java/com/example/campusmaintenance/
├── config
├── controller
├── dto
├── entity
├── enums
├── exception
├── filter
├── repository
└── service
```

## Test
```bash
mvn test
```
