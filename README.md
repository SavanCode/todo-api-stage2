# Todo API

A RESTful API for managing todo items built with Spring Boot.

## Features

- Basic CRUD operations
- Pagination support
- Filter by completion status
- Keyword search
- Swagger API documentation
- Unified response format
- CORS support

## Technology Stack

### Backend
- Java 8+
- Spring Boot 2.7.x
- Spring Data JPA
- Spring Web
- Spring Validation

### Database
- MySQL 8.0
- H2 Database (for testing)

### Build & Tools
- Maven
- Lombok
- Swagger/OpenAPI 3.0 (SpringDoc)

### Documentation
- Swagger UI
- OpenAPI Specification

## API Documentation

### Swagger UI
Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification
Access the OpenAPI specification at:
```
http://localhost:8080/v3/api-docs
```

### OpenAPI YAML
Download the OpenAPI specification in YAML format:
```
http://localhost:8080/v3/api-docs.yaml
```

## API Endpoints

### Get All Todos
```bash
GET /api/todos
```

### Get Todos with Pagination
```bash
GET /api/todos/page
```

Query Parameters:
```json
{
    "page": 0,
    "size": 10,
    "sortBy": "createdAt",
    "direction": "desc"
}
```

### Search Todos
```bash
GET /api/todos/search
```

Query Parameters:
```json
{
    "keyword": "project",
    "completed": false,
    "page": 0,
    "size": 10,
    "sortBy": "createdAt",
    "direction": "desc"
}
```

### Get Todos by Status with Pagination
```bash
GET /api/todos/status/page
```

Query Parameters:
```json
{
    "completed": false,
    "page": 0,
    "size": 10,
    "sortBy": "createdAt",
    "direction": "desc"
}
```

### Get Todo by ID
```bash
GET /api/todos/{id}
```

Path Parameters:
```json
{
    "id": 1
}
```

### Create Todo
```bash
POST /api/todos
Content-Type: application/json
```

Request Body:
```json
{
    "title": "Complete project",
    "description": "Finish the Todo API project development and testing",
    "completed": false
}
```

### Update Todo
```bash
PUT /api/todos/{id}
Content-Type: application/json
```

Path Parameters:
```json
{
    "id": 1
}
```

Request Body:
```json
{
    "title": "Updated title",
    "description": "Updated description",
    "completed": true
}
```

### Delete Todo
```bash
DELETE /api/todos/{id}
```

Path Parameters:
```json
{
    "id": 1
}
```

## Response Format

### Success Response
```json
{
    "code": 200,
    "message": "success",
    "data": {
        // Response data
    }
}
```

### Pagination Response
```json
{
    "code": 200,
    "message": "success",
    "data": {
        "content": [...],
        "pageNumber": 0,
        "pageSize": 10,
        "totalElements": 100,
        "totalPages": 10,
        "hasNext": true,
        "hasPrevious": false,
        "isFirst": true,
        "isLast": false
    }
}
```

### Error Response
```json
{
    "code": 400,
    "message": "Error message",
    "data": null
}
```

## Project Structure

```
src/main/java/com/example/todoapi/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities
├── exception/      # Custom exceptions
├── repository/     # JPA repositories
└── service/        # Business logic
```

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven
- MySQL

### Database Configuration

Configure your database connection in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todo_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### H2 Database

The application also supports H2 database for development and testing. To use H2 database, add the following configuration to `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Access the H2 console at:
```
http://localhost:8080/h2-console
```

Connection details:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

### Building the Project

```bash
mvn clean install
```

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## Error Handling

The API uses a unified error response format:

```json
{
    "code": 400,
    "message": "Error message",
    "data": null
}
```

Common HTTP status codes:
- 200: Success
- 201: Created
- 400: Bad Request
- 404: Not Found
- 500: Internal Server Error

## License

This project is licensed under the MIT License.
