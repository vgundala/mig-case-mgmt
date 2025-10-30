# Lead Management Service - Testing Guide

## Overview

This document provides comprehensive testing guidelines for the Lead Management Service microservice, including unit tests, integration tests, and API testing scripts.

## Test Structure

### Unit Tests
- **Service Layer Tests**: Test business logic in isolation
- **Controller Tests**: Test REST API endpoints with mocked dependencies
- **Repository Tests**: Test data access layer (if needed)

### Integration Tests
- **End-to-End Workflow Tests**: Test complete business processes
- **Database Integration Tests**: Test with real database (H2 in-memory)
- **Security Integration Tests**: Test authentication and authorization

### API Tests
- **Postman Collection**: Comprehensive API test suite
- **Performance Tests**: Load and stress testing scripts
- **Contract Tests**: API contract validation

## Running Tests

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- H2 Database (for integration tests)

### Unit Tests
```bash
# Run all unit tests
mvn test

# Run specific test class
mvn test -Dtest=LeadServiceTest

# Run tests with coverage
mvn test jacoco:report
```

### Integration Tests
```bash
# Run integration tests
mvn test -Dtest=*IntegrationTest

# Run with test profile
mvn test -Dspring.profiles.active=test
```

### API Tests
```bash
# Start the application
mvn spring-boot:run

# Import Postman collection and run tests
# Collection: src/test/resources/postman/Lead_Management_API_Tests.postman_collection.json
```

## Test Configuration

### Test Profiles
- **test**: Uses H2 in-memory database
- **integration-test**: Uses test database with real Oracle connection

### Test Data
- **Sample Users**: `src/test/resources/test-data/sample-users.json`
- **Sample Leads**: `src/test/resources/test-data/sample-leads.json`

## Test Categories

### 1. Unit Tests

#### Service Layer Tests
- **LeadScoringServiceTest**: Tests lead scoring algorithm
- **UserServiceTest**: Tests user management operations
- **LeadServiceTest**: Tests lead CRUD operations
- **LeadHistoryServiceTest**: Tests audit trail functionality
- **LeadDistributionServiceTest**: Tests lead distribution logic
- **WorkflowServiceTest**: Tests workflow operations

#### Controller Tests
- **AuthControllerTest**: Tests authentication endpoints
- **LeadControllerTest**: Tests lead management endpoints
- **LeadHistoryControllerTest**: Tests comment and history endpoints

### 2. Integration Tests

#### End-to-End Workflow Tests
- **Complete Lead Workflow**: Create → Update → Comment → Status Change
- **Authentication Flow**: Login → Access Protected Resources
- **Error Handling**: Invalid requests, unauthorized access
- **Pagination and Filtering**: Large dataset handling

### 3. API Tests

#### Postman Collection Structure
- **Authentication**: Login, Register, Invalid credentials
- **Lead Management**: CRUD operations, status updates
- **Lead History**: Comments, audit trail
- **Workflow Operations**: Escalation, approval, rejection
- **Error Handling**: 404, 401, 400 responses

## Test Data Management

### Test Users
```json
{
  "username": "testuser",
  "password": "password123",
  "role": "SALES_PERSON",
  "firstName": "Test",
  "lastName": "User",
  "email": "test@example.com"
}
```

### Test Leads
```json
{
  "leadName": "Test Lead",
  "company": "Test Company",
  "email": "test@testcompany.com",
  "phone": "555-9999",
  "potentialValue": 100000,
  "leadSource": "Website Signup"
}
```

## Performance Testing

### Load Testing Scripts
```bash
# Using Apache Bench
ab -n 1000 -c 10 -H "Authorization: Bearer <token>" http://localhost:8080/leads

# Using JMeter
jmeter -n -t LeadManagementLoadTest.jmx
```

### Stress Testing
- **Concurrent Users**: 100, 500, 1000
- **Request Rate**: 100, 500, 1000 requests/second
- **Database Connections**: Connection pool limits
- **Memory Usage**: Heap and non-heap memory

## Security Testing

### Authentication Tests
- **Valid Credentials**: Successful login
- **Invalid Credentials**: Failed login attempts
- **Token Expiration**: Expired token handling
- **Token Validation**: Malformed token handling

### Authorization Tests
- **Role-based Access**: SALES_PERSON vs SALES_MANAGER
- **Resource Access**: User can only access assigned leads
- **API Endpoints**: Protected vs public endpoints

### Input Validation Tests
- **SQL Injection**: Malicious input handling
- **XSS Prevention**: Script injection prevention
- **Data Validation**: Required field validation
- **Type Safety**: Data type validation

## Test Coverage

### Coverage Targets
- **Line Coverage**: > 90%
- **Branch Coverage**: > 85%
- **Method Coverage**: > 95%
- **Class Coverage**: > 90%

### Coverage Reports
```bash
# Generate coverage report
mvn test jacoco:report

# View report
open target/site/jacoco/index.html
```

## Continuous Integration

### GitHub Actions Workflow
```yaml
name: Test Suite
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '17'
      - run: mvn test
      - run: mvn jacoco:report
      - uses: codecov/codecov-action@v1
```

### Test Reports
- **Unit Test Results**: JUnit XML reports
- **Coverage Reports**: JaCoCo HTML reports
- **API Test Results**: Postman test results
- **Performance Reports**: JMeter HTML reports

## Troubleshooting

### Common Issues

#### Test Database Connection
```bash
# Check H2 console
http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# Username: sa
# Password: (empty)
```

#### Authentication Issues
```bash
# Check JWT token
echo "your-jwt-token" | base64 -d
```

#### Test Data Issues
```bash
# Reset test data
mvn clean test
```

### Debug Mode
```bash
# Run tests with debug logging
mvn test -Dlogging.level.com.mig.sales.leadmanagement=DEBUG
```

## Best Practices

### Test Design
1. **Arrange-Act-Assert**: Clear test structure
2. **Single Responsibility**: One test per scenario
3. **Descriptive Names**: Clear test method names
4. **Independent Tests**: No test dependencies
5. **Fast Execution**: Quick test feedback

### Test Data
1. **Realistic Data**: Use realistic test data
2. **Data Isolation**: Each test uses its own data
3. **Cleanup**: Clean up after tests
4. **Consistency**: Consistent data across tests

### Mocking
1. **Mock External Dependencies**: Database, external services
2. **Verify Interactions**: Check mock method calls
3. **Minimal Mocking**: Only mock what's necessary
4. **Real Objects**: Use real objects when possible

## Test Maintenance

### Regular Updates
- **Test Data**: Update test data regularly
- **Test Cases**: Add tests for new features
- **Dependencies**: Update test dependencies
- **Documentation**: Keep test documentation current

### Test Review
- **Code Review**: Include tests in code reviews
- **Test Quality**: Regular test quality assessment
- **Coverage Review**: Monitor test coverage
- **Performance Review**: Regular performance testing

## Conclusion

This testing guide provides comprehensive coverage for the Lead Management Service microservice. Follow these guidelines to ensure robust, reliable, and maintainable tests that support the development and deployment of the service.

For questions or issues, refer to the development team or create an issue in the project repository.

