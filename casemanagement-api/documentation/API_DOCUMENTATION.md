# Lead Management Service API Documentation

## Overview

The Lead Management Service is a Spring Boot microservice that provides REST APIs for managing leads, users, and lead activities in a sales pipeline. It's designed to integrate with Pega platform for workflow automation and UI components.

### Service Architecture

- **Framework**: Spring Boot 3.2.0
- **Database**: Oracle 21c
- **Authentication**: JWT (JSON Web Tokens)
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Caching**: Caffeine
- **Security**: Spring Security with role-based access control

### Base URL

- **Development**: `http://localhost:8080/api`
- **Production**: `https://api.mig.com/lead-management`

## Authentication

### JWT Authentication Flow

1. **Login**: Send credentials to `/auth/login`
2. **Receive Token**: Get JWT token in response
3. **Use Token**: Include token in Authorization header for protected endpoints

### Token Format

```
Authorization: Bearer <your-jwt-token>
```

### Token Details

- **Type**: JWT (JSON Web Token)
- **Algorithm**: HS256
- **Expiration**: 24 hours (86400000 milliseconds)
- **Claims**: User ID, username, role, expiration time

### Example Authentication

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "sales1",
    "password": "password123"
  }'

# Response
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "userId": 1,
    "username": "sales1",
    "role": "SALES_PERSON",
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@mig.com",
    "expiresIn": 86400000
  }
}

# Use token in subsequent requests
curl -X GET http://localhost:8080/api/leads \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## API Endpoints

### Authentication Endpoints

#### POST /auth/login
Authenticate user and receive JWT token.

**Request:**
```json
{
  "username": "sales1",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "userId": 1,
    "username": "sales1",
    "role": "SALES_PERSON",
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@mig.com",
    "expiresIn": 86400000
  }
}
```

#### POST /auth/register
Register a new user.

**Request:**
```json
{
  "username": "newuser",
  "password": "password123",
  "role": "SALES_PERSON",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@mig.com",
  "phone": "555-0123"
}
```

### Lead Management Endpoints

#### GET /leads
Get all leads with pagination and filtering.

**Query Parameters:**
- `status` (optional): Filter by lead status (NEW, ASSIGNED, IN_PROGRESS, PRE_CONVERSION, CONVERTED, REJECTED)
- `assignedTo` (optional): Filter by assigned user ID
- `leadSource` (optional): Filter by lead source (Partner Referral, Webinar, Website Signup, Cold Call)
- `page` (optional): Page number (0-based, default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort criteria (default: "leadScore,desc")

**Example:**
```bash
curl -X GET "http://localhost:8080/api/leads?status=NEW&page=0&size=10" \
  -H "Authorization: Bearer <token>"
```

**Response:**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [
      {
        "id": 1,
        "leadName": "John Smith",
        "company": "ABC Corp",
        "email": "john.smith@abccorp.com",
        "phone": "123-456-7890",
        "status": "NEW",
        "assignedTo": null,
        "potentialValue": 50000,
        "leadSource": "Cold Call",
        "leadScore": 20,
        "createdDate": "2024-01-15T10:30:00",
        "description": "Small business owner interested in investment services",
        "industry": "Manufacturing",
        "companySize": "Small",
        "location": "New York, NY"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalElements": 1,
    "totalPages": 1
  }
}
```

#### GET /leads/{id}
Get lead by ID.

**Example:**
```bash
curl -X GET http://localhost:8080/api/leads/1 \
  -H "Authorization: Bearer <token>"
```

#### POST /leads
Create new lead with automatic scoring.

**Request:**
```json
{
  "leadName": "Jane Doe",
  "company": "XYZ Inc",
  "email": "jane.doe@xyzinc.com",
  "phone": "098-765-4321",
  "potentialValue": 1500000,
  "leadSource": "Partner Referral",
  "description": "Large enterprise looking for comprehensive investment portfolio management",
  "industry": "Technology",
  "companySize": "Large",
  "location": "San Francisco, CA"
}
```

#### PUT /leads/{id}
Update existing lead.

#### GET /leads/my-leads
Get leads assigned to current user.

#### GET /leads/new
Get all new unassigned leads.

#### GET /leads/high-value
Get all high-value leads (>= $1M).

### Workflow Operation Endpoints

#### POST /leads/distribute
Distribute new leads to sales team (Manager only).

**Example:**
```bash
curl -X POST http://localhost:8080/api/leads/distribute \
  -H "Authorization: Bearer <token>"
```

**Response:**
```json
{
  "success": true,
  "message": "Distributed 5 leads",
  "data": "Distributed 5 leads"
}
```

#### POST /leads/{id}/escalate
Escalate high-value lead to manager (Sales Person).

**Example:**
```bash
curl -X POST http://localhost:8080/api/leads/1/escalate \
  -H "Authorization: Bearer <token>"
```

#### POST /leads/{id}/approve
Approve lead conversion (Manager only).

**Example:**
```bash
curl -X POST http://localhost:8080/api/leads/1/approve \
  -H "Authorization: Bearer <token>"
```

#### POST /leads/{id}/reject
Reject lead conversion (Manager only).

**Query Parameters:**
- `reason` (optional): Rejection reason

**Example:**
```bash
curl -X POST "http://localhost:8080/api/leads/1/reject?reason=Not%20qualified" \
  -H "Authorization: Bearer <token>"
```

#### POST /leads/{id}/request-approval
Request approval for standard lead conversion.

#### PUT /leads/{id}/status
Update lead status.

**Query Parameters:**
- `status` (required): New status (NEW, ASSIGNED, IN_PROGRESS, PRE_CONVERSION, CONVERTED, REJECTED)

**Example:**
```bash
curl -X PUT "http://localhost:8080/api/leads/1/status?status=IN_PROGRESS" \
  -H "Authorization: Bearer <token>"
```

#### POST /leads/{id}/recalculate-score
Recalculate lead score based on current data.

### Lead History Endpoints

#### GET /leads/{leadId}/history
Get lead history with pagination.

**Query Parameters:**
- `page` (optional): Page number (0-based, default: 0)
- `size` (optional): Page size (default: 20)

#### POST /leads/{leadId}/history/comments
Add comment to lead.

**Request:**
```json
{
  "commentText": "Called client, interested in our premium package",
  "action": "Phone Call"
}
```

#### GET /leads/{leadId}/history/recent
Get recent lead history (last N records).

**Query Parameters:**
- `limit` (optional): Number of recent records (default: 10)

#### GET /leads/{leadId}/history/comments
Get lead history with comments only.

#### GET /leads/{leadId}/history/count
Get total number of history records for the lead.

### Statistics Endpoints

#### GET /leads/distribution-stats
Get lead distribution statistics.

**Response:**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "activeSalesPersons": 3,
    "newLeadsCount": 5,
    "assignedLeadsCount": 12,
    "totalLeadsCount": 17
  }
}
```

## Data Models

### Lead Object
```json
{
  "id": 1,
  "leadName": "Jane Doe",
  "company": "XYZ Inc",
  "email": "jane.doe@xyzinc.com",
  "phone": "098-765-4321",
  "status": "NEW",
  "assignedTo": 1,
  "assignedToUsername": "sales1",
  "assignedToFirstName": "Jane",
  "assignedToLastName": "Smith",
  "potentialValue": 1500000,
  "leadSource": "Partner Referral",
  "leadScore": 95,
  "createdDate": "2024-01-15T10:30:00",
  "updatedDate": "2024-01-15T10:30:00",
  "description": "Large enterprise looking for comprehensive investment portfolio management",
  "industry": "Technology",
  "companySize": "Large",
  "location": "San Francisco, CA"
}
```

### Lead History Object
```json
{
  "id": 1,
  "leadId": 1,
  "userId": 1,
  "username": "sales1",
  "userFirstName": "Jane",
  "userLastName": "Smith",
  "commentText": "Called client, interested in our premium package",
  "action": "Phone Call",
  "timestamp": "2024-01-15T10:30:00",
  "actionType": "USER_ACTION",
  "oldStatus": "ASSIGNED",
  "newStatus": "IN_PROGRESS"
}
```

### User Object
```json
{
  "id": 1,
  "username": "sales1",
  "role": "SALES_PERSON",
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@mig.com",
  "phone": "555-0102",
  "isActive": true,
  "createdDate": "2024-01-15T10:30:00",
  "lastLoginDate": "2024-01-15T10:30:00"
}
```

## Error Handling

### Error Response Format
All errors return a consistent JSON response:

```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "error": "Detailed error message"
}
```

### HTTP Status Codes

- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **400 Bad Request**: Invalid request data or business rule violation
- **401 Unauthorized**: Authentication required or invalid token
- **403 Forbidden**: Access denied (insufficient permissions)
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

### Common Error Examples

#### Validation Error (400)
```json
{
  "success": false,
  "message": "Validation failed",
  "data": null,
  "error": "Lead name is required"
}
```

#### Unauthorized Access (401)
```json
{
  "success": false,
  "message": "Unauthorized access",
  "data": null,
  "error": "JWT token has expired"
}
```

#### Access Denied (403)
```json
{
  "success": false,
  "message": "Access denied",
  "data": null,
  "error": "Manager role required for this operation"
}
```

#### Resource Not Found (404)
```json
{
  "success": false,
  "message": "Resource not found",
  "data": null,
  "error": "Lead not found with id: 999"
}
```

## Rate Limiting

- **Limit**: 1000 requests per hour per user
- **Headers**: Rate limit information included in response headers
- **Exceeded**: Returns 429 Too Many Requests

## Pagination

List endpoints support pagination with the following parameters:

- `page`: Page number (0-based)
- `size`: Number of items per page
- `sort`: Sort criteria (field,direction)

**Example:**
```
GET /leads?page=0&size=20&sort=leadScore,desc
```

**Response includes pagination metadata:**
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 100,
  "totalPages": 5,
  "first": true,
  "last": false,
  "numberOfElements": 20
}
```

## Caching

The API uses caching for performance optimization:

- **User Data**: 10 minutes TTL
- **Lead Lists**: 10 minutes TTL
- **Lead Details**: 10 minutes TTL
- **Cache Headers**: Include cache control headers in responses

## Testing

### Swagger UI
Interactive API documentation available at:
- **Development**: http://localhost:8080/api/swagger-ui.html
- **Production**: https://api.mig.com/lead-management/swagger-ui.html

### Postman Collection
Import the provided Postman collection for API testing:
- Collection file: `postman/Lead-Management-API.postman_collection.json`
- Environment file: `postman/Lead-Management-API.postman_environment.json`

### cURL Examples

#### Complete Workflow Example
```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"sales1","password":"password123"}' | \
  jq -r '.data.token')

# 2. Create a lead
curl -X POST http://localhost:8080/api/leads \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "leadName": "Test Lead",
    "company": "Test Corp",
    "email": "test@testcorp.com",
    "phone": "555-0123",
    "potentialValue": 100000,
    "leadSource": "Website Signup"
  }'

# 3. Get my leads
curl -X GET http://localhost:8080/api/leads/my-leads \
  -H "Authorization: Bearer $TOKEN"

# 4. Add a comment
curl -X POST http://localhost:8080/api/leads/1/history/comments \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "commentText": "Initial contact made",
    "action": "Phone Call"
  }'
```

## Monitoring and Health Checks

### Health Check Endpoint
```
GET /actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "Oracle",
        "validationQuery": "SELECT 1"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 100000000000,
        "free": 50000000000,
        "threshold": 10485760
      }
    }
  }
}
```

### Metrics Endpoint
```
GET /actuator/metrics
```

### Application Info
```
GET /actuator/info
```

## Security Considerations

1. **HTTPS**: Always use HTTPS in production
2. **Token Storage**: Store JWT tokens securely
3. **Token Rotation**: Implement token refresh mechanism
4. **Input Validation**: All inputs are validated
5. **SQL Injection**: Protected with parameterized queries
6. **XSS Protection**: Input sanitization implemented
7. **CORS**: Configured for Pega platform origins

## Support

For technical support or questions:
- **Email**: support@mig.com
- **Documentation**: https://docs.mig.com/lead-management-api
- **Issues**: https://github.com/mig/lead-management-service/issues

