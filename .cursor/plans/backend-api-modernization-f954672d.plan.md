<!-- f954672d-4cc5-4728-ae95-29c5fdaf5f4c d3af4dff-fdfb-4b26-9501-90542f1ddf51 -->
# Backend Modernization: Spring Boot API with Pega Integration

## Overview

Transform the legacy J2EE EJB/Struts application into a modern Spring Boot REST API with JWT authentication, Oracle database integration, and hybrid Pega workflow integration. The new system will run in parallel with the existing system for validation.

## Architecture Design

### Technology Stack

- **Java 17** (LTS)
- **Spring Boot 3.2.x**
- **Spring Security 6.x** with JWT
- **Spring Data JPA** with Hibernate
- **Oracle Database 23c** (latest)
- **Flyway** for database migrations
- **SpringDoc OpenAPI** for API documentation
- **WebFlux** for async Pega integration
- **JUnit 5 + Mockito + TestContainers** for testing
- **Docker** for containerization

### Project Structure

```
case-management-api/
├── src/main/java/com/mig/sales/api/
│   ├── CaseManagementApplication.java
│   ├── controller/          # REST Controllers
│   ├── service/            # Business Logic
│   ├── repository/         # JPA Repositories
│   ├── model/             
│   │   ├── entity/        # JPA Entities
│   │   ├── dto/           # Data Transfer Objects
│   │   └── mapper/        # MapStruct Mappers
│   ├── config/            # Spring Configuration
│   ├── security/          # JWT & Security
│   ├── integration/       # Pega Integration
│   ├── event/             # Application Events
│   └── exception/         # Exception Handling
├── src/main/resources/
│   ├── application.yml
│   ├── application-dev.yml
│   ├── application-prod.yml
│   └── db/migration/      # Flyway migrations
├── src/test/java/         # Test classes
└── pom.xml
```

## Implementation Phases

### Phase 1: Project Setup & Foundation

Create new Spring Boot project structure with Maven, configure Oracle database connection, and set up basic security framework with JWT authentication.

**Files to create:**

- `pom.xml` - Maven dependencies
- `src/main/java/com/mig/sales/api/CaseManagementApplication.java` - Main Spring Boot application
- `src/main/resources/application.yml` - Configuration
- `src/main/resources/db/migration/V1__initial_schema.sql` - Database migration

**Key Dependencies:**

```xml
spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-starter-security
spring-boot-starter-validation
spring-boot-starter-webflux
ojdbc11
flyway-core
jjwt-api, jjwt-impl, jjwt-jackson
springdoc-openapi-starter-webmvc-ui
lombok
mapstruct
```

### Phase 2: Entity & Repository Layer

Migrate existing JPA entities from `case-management-ejb/src/main/java/com/mig/sales/case_management/model/` to modern Spring Data JPA with proper validation annotations.

**Entities to create:**

- `User.java` - Enhanced with roles enum
- `Lead.java` - Enhanced with status/source enums, audit fields
- `LeadHistory.java` - Enhanced with event tracking
- Repositories using Spring Data JPA interfaces

**Enhancements:**

- Add `@CreatedDate`, `@LastModifiedDate` audit fields
- Convert string constants to enums (LeadStatus, LeadSource, UserRole)
- Add validation annotations (@NotNull, @Email, @Pattern)
- Add proper bidirectional relationships

### Phase 3: Security Implementation

Implement JWT-based authentication with Spring Security, including token generation, validation, and user authentication endpoints.

**Files to create:**

- `security/JwtTokenProvider.java` - JWT token generation/validation
- `security/JwtAuthenticationFilter.java` - Filter for JWT validation
- `security/SecurityConfig.java` - Spring Security configuration
- `security/CustomUserDetailsService.java` - User loading for authentication
- `controller/AuthController.java` - Login/register endpoints
- `dto/LoginRequest.java`, `LoginResponse.java`, `RegisterRequest.java`

**Endpoints:**

- `POST /api/v1/auth/login` - Authenticate and get JWT token
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/refresh` - Refresh JWT token

### Phase 4: Core API Implementation

Implement REST controllers for lead and user management with full CRUD operations, following OpenAPI specification.

**Controllers to create:**

- `controller/LeadController.java` - Lead management endpoints
- `controller/UserController.java` - User management endpoints
- `controller/DashboardController.java` - Dashboard statistics

**Lead Endpoints:**

- `GET /api/v1/leads` - List leads with pagination, filtering, sorting
- `GET /api/v1/leads/{id}` - Get lead details
- `POST /api/v1/leads` - Create new lead
- `PUT /api/v1/leads/{id}` - Update lead
- `DELETE /api/v1/leads/{id}` - Soft delete lead
- `POST /api/v1/leads/{id}/assign` - Assign lead to user
- `POST /api/v1/leads/{id}/escalate` - Escalate lead to manager
- `POST /api/v1/leads/{id}/convert` - Convert lead
- `GET /api/v1/leads/{id}/history` - Get lead history

**DTOs to create:**

- `LeadDTO.java`, `CreateLeadRequest.java`, `UpdateLeadRequest.java`
- `UserDTO.java`, `CreateUserRequest.java`
- `LeadHistoryDTO.java`, `AddCommentRequest.java`

### Phase 5: Business Logic Services

Implement service layer with business logic from existing EJB beans, including lead scoring, distribution, and workflow management.

**Services to create:**

- `service/LeadService.java` - Lead management business logic
- `service/LeadScoringService.java` - Lead scoring algorithm
- `service/LeadDistributionService.java` - Round-robin distribution
- `service/UserService.java` - User management
- `service/WorkflowService.java` - Workflow coordination

**Key Business Logic to migrate:**

- Lead scoring algorithm from `LeadScoringService.java`
- Round-robin distribution from `DistributeLeadsAction.java`
- Lead validation and status transitions
- Permission checks (sales person vs manager)

### Phase 6: Pega Integration Layer

Implement hybrid integration with Pega using synchronous REST calls for critical operations and asynchronous events for notifications.

**Files to create:**

- `integration/PegaIntegrationService.java` - Pega REST client
- `integration/PegaWebClientConfig.java` - WebClient configuration
- `integration/dto/PegaLeadRequest.java` - Pega request DTOs
- `integration/dto/PegaLeadResponse.java` - Pega response DTOs
- `controller/PegaWebhookController.java` - Webhook endpoints for Pega callbacks
- `event/LeadCreatedEvent.java`, `LeadAssignedEvent.java`, etc.
- `event/LeadEventHandler.java` - Event listener for async Pega updates

**Synchronous Operations (critical path):**

- Lead creation - start Pega workflow
- Lead escalation - update Pega workflow
- Lead conversion approval - wait for Pega response

**Asynchronous Operations (notifications):**

- Lead status updates
- Comment additions
- Assignment changes

**Webhook Endpoints:**

- `POST /api/v1/webhooks/pega/lead-updated` - Receive updates from Pega
- `POST /api/v1/webhooks/pega/workflow-completed` - Workflow completion notification

### Phase 7: Exception Handling & Validation

Implement global exception handling, custom exceptions, and comprehensive validation.

**Files to create:**

- `exception/GlobalExceptionHandler.java` - @ControllerAdvice for global handling
- `exception/LeadNotFoundException.java`
- `exception/UserNotFoundException.java`
- `exception/BusinessValidationException.java`
- `exception/PegaIntegrationException.java`
- `dto/ErrorResponse.java` - Standard error response structure
- `validation/LeadValidator.java` - Custom validation logic

### Phase 8: OpenAPI Documentation

Configure SpringDoc OpenAPI for interactive API documentation and contract testing.

**Files to create:**

- `config/OpenApiConfig.java` - OpenAPI configuration
- Add @Operation, @ApiResponse annotations to all controllers
- Generate OpenAPI specification file for Pega team

**Documentation includes:**

- Complete API reference at `/swagger-ui.html`
- OpenAPI JSON at `/v3/api-docs`
- Authentication flows
- Request/response examples

### Phase 9: Comprehensive Testing

Implement full test suite with unit tests, integration tests, and Pega contract tests.

**Test Structure:**

```
src/test/java/
├── unit/
│   ├── service/          # Service layer unit tests
│   └── security/         # Security unit tests
├── integration/
│   ├── controller/       # API endpoint integration tests
│   ├── repository/       # Database integration tests
│   └── pega/            # Pega integration contract tests
└── testcontainers/      # TestContainers configuration
```

**Test Files to create:**

- `LeadServiceTest.java` - Unit tests with Mockito
- `LeadControllerIntegrationTest.java` - @SpringBootTest integration tests
- `PegaIntegrationServiceTest.java` - Mock Pega responses with WireMock
- `AuthenticationFlowTest.java` - End-to-end auth tests
- `DatabaseMigrationTest.java` - Flyway migration tests
- `AbstractIntegrationTest.java` - Base class with TestContainers

### Phase 10: Deployment & Monitoring

Configure containerization, monitoring, and production deployment.

**Files to create:**

- `Dockerfile` - Multi-stage Docker build
- `docker-compose.yml` - Local development setup
- `docker-compose.prod.yml` - Production configuration
- `.github/workflows/ci-cd.yml` - CI/CD pipeline
- `config/ActuatorConfig.java` - Health checks and metrics
- `config/LoggingConfig.java` - Structured logging

**Monitoring Configuration:**

- Spring Boot Actuator endpoints
- Prometheus metrics at `/actuator/prometheus`
- Health checks at `/actuator/health`
- Custom metrics for Pega integration success/failure rates
- Structured JSON logging with correlation IDs

## Database Migration Strategy

Keep existing Oracle tables and schema, add new columns gradually with Flyway migrations:

**Migration Files:**

- `V1__initial_schema.sql` - Document existing schema
- `V2__add_audit_columns.sql` - Add created_date, updated_date
- `V3__add_indexes.sql` - Performance indexes
- `V4__add_pega_workflow_id.sql` - Track Pega workflow IDs

## Parallel Run Strategy

1. **Phase 1-2 months**: New API development and testing
2. **Phase 3-4 months**: Deploy new API alongside existing system
3. **Month 5**: Pega team updates to use new APIs
4. **Month 6**: Validation and comparison of both systems
5. **Month 7**: Complete cutover, decommission old system

**Data Consistency:**

- Both systems read/write to same Oracle database
- New API adds audit columns that old system ignores
- Use database triggers if needed for cross-system sync

## Configuration Management

**Environment-specific configurations:**

- `application-dev.yml` - Local development
- `application-test.yml` - Testing environment
- `application-prod.yml` - Production environment

**Externalized Secrets:**

- Database credentials via environment variables
- JWT secret key via environment variables
- Pega API keys via environment variables

## API Versioning Strategy

All APIs under `/api/v1/` prefix for future versioning capability.

## Success Criteria

1. All existing functionality from Struts/EJB app available via REST APIs
2. JWT authentication working with proper token refresh
3. Pega integration tested with mock responses
4. 80%+ code coverage with tests
5. API documentation complete and accessible
6. Docker containers running successfully
7. Load testing shows acceptable performance (< 200ms for 95th percentile)
8. Parallel run shows data consistency between systems

## Key Files from Legacy System to Reference

Existing files to migrate business logic from:

- `case-management-ejb/src/main/java/com/mig/sales/case_management/service/LeadService.java`
- `case-management-ejb/src/main/java/com/mig/sales/case_management/service/LeadScoringService.java`
- `case-management-ejb/src/main/java/com/mig/sales/case_management/service/UserService.java`
- `case-management-web/src/main/java/com/mig/sales/case_management/action/*.java`
- `case-management/db_setup.sql` - Current database schema

### To-dos

- [ ] Create Spring Boot project structure with Maven, dependencies, and base configuration files
- [ ] Configure Oracle database connection, Flyway migrations, and document existing schema
- [ ] Create JPA entities (User, Lead, LeadHistory) with enums, validation, and Spring Data repositories
- [ ] Implement JWT authentication with Spring Security including token provider, filters, and auth endpoints
- [ ] Create DTOs and MapStruct mappers for all entities and API requests/responses
- [ ] Implement service layer with business logic: LeadService, UserService, LeadScoringService, LeadDistributionService
- [ ] Create REST controllers for leads, users, dashboard with full CRUD operations and pagination
- [ ] Implement global exception handler, custom exceptions, and validation framework
- [ ] Implement Pega integration service with WebClient, DTOs, webhook endpoints, and event handling
- [ ] Configure OpenAPI documentation with SpringDoc and add annotations to all controllers
- [ ] Write unit tests for services, security, and business logic with Mockito
- [ ] Write integration tests for API endpoints and repositories using TestContainers
- [ ] Write Pega integration contract tests with WireMock for all integration scenarios
- [ ] Create Dockerfile, docker-compose files for development and production environments
- [ ] Configure Spring Boot Actuator, Prometheus metrics, health checks, and structured logging
- [ ] Create CI/CD pipeline configuration with build, test, and deployment stages