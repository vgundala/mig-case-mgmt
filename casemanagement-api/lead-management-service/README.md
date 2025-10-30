# Lead Management Service

A Spring Boot microservice for lead management system integration with Pega platform.

## Overview

This microservice provides REST APIs for managing leads, users, and lead activities in a sales pipeline. It's designed to integrate with Pega platform for workflow automation and UI components.

## Features

- **JWT Authentication**: Secure API access with JWT tokens
- **Lead Management**: CRUD operations for leads with scoring and distribution
- **User Management**: User authentication and role-based access control
- **Lead History**: Activity tracking and audit trail
- **Workflow Operations**: Escalation, approval, and distribution workflows
- **Caching**: Performance optimization with Caffeine cache
- **OpenAPI Documentation**: Swagger UI for API testing and documentation

## Technology Stack

- **Spring Boot**: 3.2.0
- **Java**: 17
- **Database**: Oracle 21c
- **Security**: Spring Security with JWT
- **Cache**: Caffeine
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Build Tool**: Maven

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Lead Management
- `GET /api/leads` - Get all leads (with pagination and filtering)
- `GET /api/leads/{id}` - Get lead by ID
- `POST /api/leads` - Create new lead
- `PUT /api/leads/{id}` - Update lead
- `GET /api/leads/my-leads` - Get current user's assigned leads
- `GET /api/leads/new` - Get new unassigned leads
- `GET /api/leads/high-value` - Get high-value leads (>= $1M)

### Workflow Operations
- `POST /api/leads/distribute` - Distribute leads to sales team (Manager only)
- `POST /api/leads/{id}/escalate` - Escalate high-value lead (Sales Person)
- `POST /api/leads/{id}/approve` - Approve lead conversion (Manager only)
- `POST /api/leads/{id}/reject` - Reject lead conversion (Manager only)
- `POST /api/leads/{id}/request-approval` - Request approval for standard lead

### Lead History
- `GET /api/leads/{leadId}/history` - Get lead history
- `POST /api/leads/{leadId}/history/comments` - Add comment to lead

## Configuration

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:xe
    username: lead_mgmt_user
    password: password
    driver-class-name: oracle.jdbc.OracleDriver
```

### JWT Configuration
```yaml
spring:
  security:
    jwt:
      secret: mySecretKey123456789012345678901234567890
      expiration: 86400000 # 24 hours
```

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Oracle Database 21c

### Build and Run
```bash
# Build the application
mvn clean package

# Run the application
mvn spring-boot:run

# Or run the JAR file
java -jar target/lead-management-service-1.0.0.jar
```

### Docker (Optional)
```bash
# Build Docker image
docker build -t lead-management-service .

# Run Docker container
docker run -p 8080:8080 lead-management-service
```

## API Documentation

Once the application is running, you can access:
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

## Security

The application uses JWT tokens for authentication. To access protected endpoints:

1. Login using `/api/auth/login` to get a JWT token
2. Include the token in the Authorization header: `Bearer <token>`

## Monitoring

The application includes Spring Boot Actuator endpoints:
- **Health Check**: http://localhost:8080/api/actuator/health
- **Metrics**: http://localhost:8080/api/actuator/metrics
- **Info**: http://localhost:8080/api/actuator/info

## Profiles

- **dev**: Development configuration with detailed logging
- **test**: Test configuration with H2 in-memory database
- **prod**: Production configuration with external database

## Error Handling

The application provides consistent error responses:
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "error": "Detailed error message"
}
```

## Caching

The application uses Caffeine cache for:
- User data (10 minutes TTL)
- Lead lists (10 minutes TTL)
- Lead details (10 minutes TTL)

## Database Schema

The application expects the following Oracle tables:
- `APP_USERS` - User accounts
- `LEADS` - Lead information
- `LEAD_HISTORY` - Lead activity audit trail

See `documentation/pega_oracle_ddl.sql` for the complete schema.

## Integration with Pega

This microservice is designed to integrate with Pega platform:
- REST APIs for data access
- JWT authentication
- JSON responses compatible with Pega data classes
- CORS enabled for Pega origins

## License

MIT License - see LICENSE file for details.

