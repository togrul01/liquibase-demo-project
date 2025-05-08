# Spring Liquibase Project

This project demonstrates a Spring Boot application with Liquibase integration for database migrations.

## Features

- Card Management (CRUD operations)
- User Management (CRUD operations)
- Database migrations with Liquibase
- Swagger/OpenAPI documentation
- Request/Response logging with masking of sensitive data
- Pagination support
- Exception handling

## Prerequisites

- Java 17
- Maven
- PostgreSQL
- Postman (for API testing)

## Getting Started

1. Clone the repository
2. Configure your database connection in `application.properties`
3. Run the application:
```bash
mvn spring-boot:run
```

## API Documentation

### Swagger UI
Access the Swagger UI at: http://localhost:8080/swagger-ui.html

### Postman Collection
The project includes a Postman collection for testing the APIs. You can find it in the `postman` directory.

To use the collection:
1. Import `postman/spring-liquibase-api.json` into Postman
2. Set up the environment variable `baseUrl` (default: http://localhost:8080)
3. Start making API requests

The collection includes endpoints for:
- Card Management (CRUD operations)
- User Management (CRUD operations)

## API Endpoints

### Card Management
- `POST /v1/cards` - Create a new card
- `GET /v1/cards/{id}` - Get card by ID
- `GET /v1/cards` - Get all cards
- `GET /v1/cards/getCards` - Get paginated cards
- `PUT /v1/cards/{id}` - Update a card
- `DELETE /v1/cards/{id}` - Delete a card

### User Management
- `POST /v1/users` - Create a new user
- `GET /v1/users/{id}` - Get user by ID
- `GET /v1/users/getUsers` - Get paginated users

## Security Features

- Card numbers are masked in logs
- Input validation for all requests
- Exception handling for various scenarios

## Database Migrations

The project uses Liquibase for database migrations. Migration files are located in:
```
src/main/resources/db/changelog/
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request 