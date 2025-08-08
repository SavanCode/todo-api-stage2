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
- [Testing](#testing)
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

## Testing

### Test Overview
The project includes comprehensive unit tests covering all layers of the application:
- **Repository Layer**: Database integration tests
- **Service Layer**: Business logic unit tests
- **Controller Layer**: Web layer tests
- **Application**: Context loading tests

### Running Tests

#### Run All Tests
```bash
mvn test
```

#### Quick Test Verification
To quickly verify that all tests are passing:
```bash
./mvnw test | grep -E "(Tests run|BUILD|Results:)"
```

Or use the provided verification script:
```bash
./test-verification.sh
```

Expected output for successful tests:
```
[INFO] Tests run: 57, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

The verification script will show:
```
🧪 Running Todo API Tests...
================================
📊 Test Results:
[INFO] Tests run: 57, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS

✅ All tests passed successfully!
🎉 Your Todo API is working correctly.
```

#### Run Specific Test Classes
```bash
# Run only repository tests
mvn test -Dtest=*RepositoryTest

# Run only service tests
mvn test -Dtest=*ServiceImplTest

# Run only controller tests
mvn test -Dtest=*ControllerTest
```

#### Run Individual Test Methods
```bash
# Run specific test method
mvn test -Dtest=UserServiceImplTest#testRegisterUser

# Run multiple test methods
mvn test -Dtest=UserServiceImplTest#testRegisterUser,UserServiceImplTest#testLoginUser
```

### Test Results Interpretation

#### Successful Test Run
When all tests pass, you should see output similar to:
```
[INFO] Tests run: 57, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 57, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

**Key indicators of success:**
- `Failures: 0` - No test failures
- `Errors: 0` - No test errors
- `BUILD SUCCESS` - All tests passed
- Total tests run should be 57 (may vary with future additions)

#### Failed Test Run
If tests fail, you'll see output like:
```
[INFO] Tests run: 57, Failures: 2, Errors: 1, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Failed tests:
[INFO]   UserServiceImplTest.testLoginUser
[INFO]   TodoServiceImplTest.testCreateTodo
[INFO] 
[INFO] Tests in error:
[INFO]   AuthControllerTest.testRegisterEndpoint
[INFO] 
[INFO] Tests run: 57, Failures: 2, Errors: 1, Skipped: 0
[INFO] 
[INFO] BUILD FAILURE
```

### Test Structure

#### Repository Tests (`@DataJpaTest`)
Located in `src/test/java/com/example/todoapi/repository/`

**UserRepositoryTest** (12 tests):
- `save_shouldCreateUser` - Test user creation
- `findById_shouldReturnUser_whenUserExists` - Test user retrieval
- `findByUsername_shouldReturnUser_whenUserExists` - Test username lookup
- `existsByEmail_shouldReturnTrue_whenEmailExists` - Test email existence
- `existsByEmail_shouldReturnFalse_whenEmailDoesNotExist` - Test non-existent email
- `existsByUsername_shouldReturnTrue_whenUsernameExists` - Test username existence
- `existsByUsername_shouldReturnFalse_whenUsernameDoesNotExist` - Test non-existent username
- `delete_shouldRemoveUser` - Test user deletion
- `findAll_shouldReturnAllUsers` - Test bulk retrieval

**TodoRepositoryTest** (12 tests):
- `save_shouldCreateTodo` - Test todo creation
- `findById_shouldReturnTodo_whenTodoExists` - Test todo retrieval
- `findByUserId_shouldReturnUserTodos` - Test user-specific todos
- `findByUserIdAndCompleted_shouldReturnFilteredTodos` - Test filtered queries
- `findByUserId_shouldReturnEmpty_whenUserHasNoTodos` - Test empty results
- `update_shouldModifyTodo` - Test todo updates
- `delete_shouldRemoveTodo` - Test todo deletion
- `findByUserIdWithPagination_shouldReturnPaginatedResults` - Test pagination

#### Service Tests (`@ExtendWith(MockitoExtension.class)`)
Located in `src/test/java/com/example/todoapi/service/impl/`

**UserServiceImplTest** (11 tests):
- `testRegisterUser` - Test user registration
- `testRegisterUserWithExistingUsername` - Test duplicate username handling
- `testRegisterUserWithExistingEmail` - Test duplicate email handling
- `testLoginUser` - Test user login
- `testLoginUserWithInvalidCredentials` - Test invalid login
- `testGenerateJwtToken` - Test JWT token generation
- `testValidateJwtToken` - Test JWT token validation
- `testLoadUserByUsername` - Test user loading
- `testLoadUserByUsernameNotFound` - Test non-existent user loading

**TodoServiceImplTest** (14 tests):
- `testCreateTodo` - Test todo creation
- `testCreateTodoWithInvalidData` - Test validation
- `testGetTodoById` - Test todo retrieval
- `testGetTodoByIdNotFound` - Test non-existent todo
- `testGetTodoByIdUnauthorized` - Test unauthorized access
- `testUpdateTodo` - Test todo updates
- `testUpdateTodoNotFound` - Test updating non-existent todo
- `testDeleteTodo` - Test todo deletion
- `testGetTodosByUser` - Test user-specific todos
- `testGetTodosByUserWithPagination` - Test paginated results
- `testGetTodosByStatus` - Test status filtering
- `testMarkTodoAsCompleted` - Test completion status
- `testSearchTodos` - Test search functionality

#### Controller Tests (`@WebMvcTest`)
Located in `src/test/java/com/example/todoapi/controller/`

**AuthControllerTest** (5 tests):
- `testRegisterEndpoint` - Test registration endpoint
- `testRegisterEndpointWithInvalidData` - Test validation
- `testLoginEndpoint` - Test login endpoint
- `testLoginEndpointWithInvalidCredentials` - Test invalid login
- `testLoginEndpointWithMissingData` - Test missing data handling

**HelloControllerTest** (1 test):
- `testHelloEndpoint` - Test basic endpoint functionality

#### Application Tests
Located in `src/test/java/com/example/todoapi/`

**TodoApiApplicationTests** (1 test):
- `contextLoads` - Test Spring context loading

### Test Configuration

#### Test Dependencies
The project includes the following test dependencies:
- **JUnit 5**: Core testing framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Integration testing support
- **Spring Security Test**: Security testing utilities
- **H2 Database**: In-memory database for testing

#### Test Properties
Tests use a separate configuration in `src/test/resources/application.properties`:
- H2 in-memory database
- Disabled security for certain tests
- Test-specific logging configuration

### Continuous Integration

#### GitHub Actions (Recommended)
Create `.github/workflows/test.yml`:
```yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Run tests
      run: mvn test
```

#### Local Development
For development, you can run tests with different verbosity levels:

```bash
# Quiet mode (minimal output)
mvn test -q

# Verbose mode (detailed output)
mvn test -X

# Run tests and generate coverage report
mvn test jacoco:report
```

### Troubleshooting

#### Common Test Issues

1. **Database Connection Issues**
   ```bash
   # Clean and rebuild
   mvn clean test
   ```

2. **Security Configuration Issues**
   - Ensure `@WithMockUser` annotations are used for secured endpoints
   - Check that security filters are properly excluded in controller tests

3. **Mock Configuration Issues**
   - Verify that all dependencies are properly mocked
   - Check that mock behavior is correctly defined

4. **Test Data Issues**
   - Ensure test data is properly set up in `@BeforeEach` methods
   - Verify that test isolation is maintained

#### Test Logs
Enable debug logging for tests by adding to `src/test/resources/application.properties`:
```properties
logging.level.com.example.todoapi=DEBUG
logging.level.org.springframework.test=DEBUG
```

## Development

### Running Tests
```bash
mvn test
```

For quick test verification:
```bash
./test-verification.sh
```

### Code Style
The project follows Google Java Style Guide. To format your code:
```bash
mvn spotless:apply
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
