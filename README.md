# Todo API

A RESTful API for managing todo items built with Spring Boot.

## Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Response Format](#response-format)
- [Project Structure](#project-structure)
- [Database Configuration](#database-configuration)
- [Security](#security)
- [Development](#development)
- [License](#license)

## Features

- **Todo Management**
  - Create, read, update, and delete todo items
  - Mark todos as complete/incomplete
  - Set due dates and priorities
  - Add descriptions and notes

- **Advanced Queries**
  - Pagination support for large datasets
  - Filter todos by completion status
  - Search todos by keyword
  - Sort todos by various fields

- **Security**
  - JWT-based authentication
  - Role-based access control
  - Secure password storage
  - Token-based session management

- **Documentation**
  - Interactive API documentation with Swagger UI
  - OpenAPI 3.0 specification
  - Detailed endpoint descriptions
  - Request/response examples

## Technology Stack

### Backend
- Java 17
- Spring Boot 2.7.18
- Spring Security
- Spring Data JPA
- Spring Validation

### Database
- H2 Database (Development)
- JPA/Hibernate

### Documentation
- Swagger/OpenAPI 3.0
- SpringDoc

### Build Tools
- Maven
- Lombok

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Your favorite IDE (IntelliJ IDEA, Eclipse, etc.)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/todo-api.git
   cd todo-api
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8080.

## API Documentation

### Swagger UI
Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI Specification
Access the OpenAPI specification at:
```
http://localhost:8080/v3/api-docs
```

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Todo Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/todos` | Get all todos |
| GET | `/api/todos/page` | Get paginated todos |
| GET | `/api/todos/search` | Search todos with pagination |
| GET | `/api/todos/status/page` | Get todos by status with pagination |
| POST | `/api/todos` | Create a new todo |
| PUT | `/api/todos/{id}` | Update a todo |
| DELETE | `/api/todos/{id}` | Delete a todo |

## Response Format

### Success Response
```json
{
    "code": 200,
    "message": "Success",
    "data": {
        // Response data
    }
}
```

### Pagination Response
```json
{
    "code": 200,
    "message": "Success",
    "data": {
        "content": [],
        "pageNumber": 0,
        "pageSize": 10,
        "totalElements": 0,
        "totalPages": 0,
        "hasNext": false,
        "hasPrevious": false,
        "isFirst": true,
        "isLast": true
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
│   ├── OpenApiConfig.java
│   ├── SecurityConfig.java
│   └── WebConfig.java
├── controller/      # REST controllers
│   ├── AuthController.java
│   └── TodoController.java
├── dto/            # Data Transfer Objects
│   ├── auth/
│   └── todo/
├── entity/         # JPA entities
│   ├── Todo.java
│   └── User.java
├── repository/     # JPA repositories
│   ├── TodoRepository.java
│   └── UserRepository.java
├── security/       # Security related classes
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenUtil.java
│   └── UserDetailsServiceImpl.java
├── service/        # Business logic
│   ├── impl/
│   ├── TodoService.java
│   └── UserService.java
└── exception/      # Exception handling
    └── GlobalExceptionHandler.java
```

## Database Configuration

### H2 Database (Development)
The application uses H2 database by default. Access the H2 console at:
```
http://localhost:8080/h2-console
```

H2 Console Configuration:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## Security

### JWT Authentication
The API uses JWT (JSON Web Token) for authentication. To access protected endpoints:

1. Register a new user:
   ```bash
   POST /api/auth/register
   {
       "username": "user",
       "email": "user@example.com",
       "password": "password123"
   }
   ```

2. Login to get JWT token:
   ```bash
   POST /api/auth/login
   {
       "username": "user",
       "password": "password123"
   }
   ```

3. Include the token in requests:
   ```
   Authorization: Bearer <your_jwt_token>
   ```

## Development

### Running Tests
```bash
mvn test
```

### Code Style
The project follows Google Java Style Guide. To format your code:
```bash
mvn spotless:apply
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
