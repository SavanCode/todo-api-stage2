# Todo API

A RESTful API for managing todo items built with Spring Boot.

## Technology Stack

- Java 8+
- Spring Boot 2.7.x
- Spring Data JPA
- MySQL
- Maven
- Lombok
- Swagger/OpenAPI 3.0

## Features

- CRUD operations for todo items
- Filter todos by completion status
- Parameter validation
- Unified exception handling
- RESTful design
- Swagger API documentation
- Standardized API response format

## Swagger Documentation

The project uses SpringDoc OpenAPI (Swagger) for API documentation. The documentation is automatically generated from the code annotations.

### Accessing Swagger UI

You can access the Swagger UI interface at:
```
Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/api-docs
OpenAPI YAML: http://localhost:8080/api-docs.yaml

```

### Swagger Configuration

The Swagger configuration is defined in `OpenApiConfig.java` and includes:
- API title and description
- Version information
- Contact details
- License information
- Server configuration

### Available Documentation

The Swagger UI provides:
- Interactive API documentation
- Request/response schemas
- Example requests
- Try-it-out functionality
- Authentication information (if configured)

### API Documentation Features

- Detailed endpoint descriptions
- Request/response examples
- Parameter validation rules
- Response codes and meanings
- Data models and schemas

## API Documentation

The API documentation is available through Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

### API Response Format

All API responses follow a standardized format:

```json
{
    "status": 200,
    "message": "Success",
    "data": {
        // Response data
    },
    "timestamp": "2024-01-01T12:00:00"
}
```

### Available Endpoints

#### Get All Todos
```bash
GET /api/todos
```

#### Get Todo by ID
```bash
GET /api/todos/{id}
```

#### Create Todo
```bash
POST /api/todos
Content-Type: application/json

{
    "title": "Complete project",
    "description": "Finish the todo API project",
    "completed": false
}
```

#### Update Todo
```bash
PUT /api/todos/{id}
Content-Type: application/json

{
    "title": "Updated title",
    "description": "Updated description",
    "completed": true
}
```

#### Delete Todo
```bash
DELETE /api/todos/{id}
```

#### Filter Todos by Status
```bash
GET /api/todos/status?completed=true
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

The API uses a standardized error response format:

```json
{
    "status": 400,
    "message": "Error message",
    "data": null,
    "timestamp": "2024-01-01T12:00:00"
}
```

Common HTTP status codes:
- 200: Success
- 201: Created
- 400: Bad Request
- 404: Not Found
- 500: Internal Server Error

## Testing Functionality

After starting the application, you can test it using the following methods:

Using curl commands:
```bash
# Create a TODO
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Spring Boot","description":"Complete TODO List project"}'

# Get all TODOs
curl http://localhost:8080/api/todos

# Update TODO status
curl -X PUT http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Spring Boot","description":"Complete TODO List project","completed":true}'
```

## H2 Database

Access the H2 console:

```bash
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (empty)
```

## License

This project is licensed under the MIT License. 