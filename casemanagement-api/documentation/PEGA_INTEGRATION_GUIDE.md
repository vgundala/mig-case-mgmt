# Pega Integration Guide - Lead Management Service

## Overview

This guide provides comprehensive instructions for integrating the Lead Management Service with Pega platform. The microservice exposes REST APIs that Pega can consume to manage leads, users, and workflow operations.

## Table of Contents

1. [Connection Configuration](#connection-configuration)
2. [Authentication Setup](#authentication-setup)
3. [Data Mapping](#data-mapping)
4. [Integration Patterns](#integration-patterns)
5. [Error Handling](#error-handling)
6. [Testing](#testing)
7. [Best Practices](#best-practices)

## Connection Configuration

### Base URLs

- **Development**: `http://localhost:8080/api`
- **Production**: `https://api.mig.com/lead-management`

### Connection Settings

| Setting | Value | Description |
|---------|-------|-------------|
| Base URL | `https://api.mig.com/lead-management` | API base URL |
| Timeout | 30 seconds | Request timeout |
| Retry Attempts | 3 | Number of retry attempts |
| Retry Delay | 1 second | Delay between retries |
| Max Connections | 100 | Maximum concurrent connections |

### Headers Configuration

All API requests must include the following headers:

```http
Content-Type: application/json
Authorization: Bearer <jwt-token>
Accept: application/json
```

## Authentication Setup

### JWT Token Management

#### 1. Configure Authentication Connector

In Pega, create a REST connector for authentication:

**Connector Name**: `LeadManagementAuth`
**Connector Type**: REST
**Base URL**: `https://api.mig.com/lead-management`

#### 2. Login Endpoint Configuration

**Method**: POST
**URL**: `/auth/login`
**Request Body**:
```json
{
  "username": "{{.pyUsername}}",
  "password": "{{.pyPassword}}"
}
```

**Response Mapping**:
```json
{
  "token": "{{.data.token}}",
  "userId": "{{.data.userId}}",
  "username": "{{.data.username}}",
  "role": "{{.data.role}}",
  "expiresIn": "{{.data.expiresIn}}"
}
```

#### 3. Token Storage

Store the JWT token in a data page or property:

**Data Page**: `D_LeadManagementAuth`
**Properties**:
- `pyToken` (Text)
- `pyTokenExpiry` (DateTime)
- `pyUserId` (Integer)
- `pyUsername` (Text)
- `pyRole` (Text)

#### 4. Token Refresh Logic

Create a decision rule to check token expiry and refresh if needed:

```java
// Check if token is expired
if (D_LeadManagementAuth.pyTokenExpiry < DateTime.now()) {
    // Call login endpoint to get new token
    // Update D_LeadManagementAuth data page
}
```

### Authentication Flow

1. **Initial Login**: User logs into Pega
2. **Token Retrieval**: Pega calls `/auth/login` with user credentials
3. **Token Storage**: Store JWT token in data page
4. **API Calls**: Include token in Authorization header for all API calls
5. **Token Refresh**: Check expiry and refresh token as needed

## Data Mapping

### Pega Data Classes

#### Data-Lead Class

Map Oracle LEADS table to Pega Data-Lead class:

| Pega Property | Oracle Column | Type | Description |
|---------------|---------------|------|-------------|
| `pyLeadID` | LEAD_ID | Integer | Primary key |
| `pyLeadName` | LEAD_NAME | Text | Lead name |
| `pyCompany` | COMPANY | Text | Company name |
| `pyEmail` | EMAIL | Text | Email address |
| `pyPhone` | PHONE | Text | Phone number |
| `pyStatus` | STATUS | Text | Lead status |
| `pyAssignedTo` | ASSIGNED_TO | Integer | Assigned user ID |
| `pyAssignedToUsername` | - | Text | Assigned user username |
| `pyPotentialValue` | POTENTIAL_VALUE | Decimal | Potential deal value |
| `pyLeadSource` | LEAD_SOURCE | Text | Lead source |
| `pyLeadScore` | LEAD_SCORE | Integer | Calculated score |
| `pyCreatedDate` | CREATED_DATE | DateTime | Creation date |
| `pyUpdatedDate` | UPDATED_DATE | DateTime | Last update date |
| `pyDescription` | DESCRIPTION | Text | Lead description |
| `pyIndustry` | INDUSTRY | Text | Industry |
| `pyCompanySize` | COMPANY_SIZE | Text | Company size |
| `pyLocation` | LOCATION | Text | Location |

#### Data-User Class

Map Oracle APP_USERS table to Pega Data-User class:

| Pega Property | Oracle Column | Type | Description |
|---------------|---------------|------|-------------|
| `pyUserID` | USER_ID | Integer | Primary key |
| `pyUsername` | USERNAME | Text | Username |
| `pyRole` | ROLE | Text | User role |
| `pyFirstName` | FIRST_NAME | Text | First name |
| `pyLastName` | LAST_NAME | Text | Last name |
| `pyEmail` | EMAIL | Text | Email address |
| `pyPhone` | PHONE | Text | Phone number |
| `pyIsActive` | IS_ACTIVE | Boolean | Active status |

#### Data-LeadHistory Class

Map Oracle LEAD_HISTORY table to Pega Data-LeadHistory class:

| Pega Property | Oracle Column | Type | Description |
|---------------|---------------|------|-------------|
| `pyHistoryID` | HISTORY_ID | Integer | Primary key |
| `pyLeadID` | LEAD_ID | Integer | Lead reference |
| `pyUserID` | USER_ID | Integer | User reference |
| `pyUsername` | - | Text | Username |
| `pyCommentText` | COMMENT_TEXT | Text | Comment text |
| `pyAction` | ACTION | Text | Action description |
| `pyTimestamp` | TIMESTAMP | DateTime | Action timestamp |
| `pyActionType` | ACTION_TYPE | Text | Action type |
| `pyOldStatus` | OLD_STATUS | Text | Previous status |
| `pyNewStatus` | NEW_STATUS | Text | New status |

## Integration Patterns

### 1. Creating a Lead from Pega Case

#### Connector Configuration

**Connector Name**: `LeadManagementCreateLead`
**Method**: POST
**URL**: `/leads`
**Headers**: Include JWT token from authentication

#### Request Mapping

```java
// Map Pega case properties to API request
{
  "leadName": "{{.pyLeadName}}",
  "company": "{{.pyCompany}}",
  "email": "{{.pyEmail}}",
  "phone": "{{.pyPhone}}",
  "potentialValue": "{{.pyPotentialValue}}",
  "leadSource": "{{.pyLeadSource}}",
  "description": "{{.pyDescription}}",
  "industry": "{{.pyIndustry}}",
  "companySize": "{{.pyCompanySize}}",
  "location": "{{.pyLocation}}"
}
```

#### Response Mapping

```java
// Map API response to Pega case properties
pyLeadID = {{.data.id}}
pyLeadScore = {{.data.leadScore}}
pyStatus = {{.data.status}}
pyCreatedDate = {{.data.createdDate}}
```

#### Implementation Steps

1. **Create Connector**: Configure REST connector for lead creation
2. **Map Properties**: Map case properties to API request format
3. **Handle Response**: Map API response back to case properties
4. **Error Handling**: Handle API errors and validation failures
5. **Update Case**: Update case with lead information

### 2. Retrieving Lead Details for Display

#### Connector Configuration

**Connector Name**: `LeadManagementGetLead`
**Method**: GET
**URL**: `/leads/{{.pyLeadID}}`

#### Response Mapping

```java
// Map API response to display properties
pyLeadName = {{.data.leadName}}
pyCompany = {{.data.company}}
pyEmail = {{.data.email}}
pyPhone = {{.data.phone}}
pyStatus = {{.data.status}}
pyAssignedToUsername = {{.data.assignedToUsername}}
pyPotentialValue = {{.data.potentialValue}}
pyLeadSource = {{.data.leadSource}}
pyLeadScore = {{.data.leadScore}}
pyDescription = {{.data.description}}
pyIndustry = {{.data.industry}}
pyCompanySize = {{.data.companySize}}
pyLocation = {{.data.location}}
```

### 3. Getting User's Assigned Leads

#### Connector Configuration

**Connector Name**: `LeadManagementGetMyLeads`
**Method**: GET
**URL**: `/leads/my-leads`

#### Response Mapping

```java
// Map API response to lead list
pyLeadList = {{.data}}
```

#### Implementation in Section

```html
<!-- Lead List Section -->
<pega:reference name="D_LeadManagementGetMyLeads.pyLeadList">
  <pega:foreach name=".pyLeadList" item="lead">
    <div class="lead-item">
      <h3>{{lead.pyLeadName}}</h3>
      <p>Company: {{lead.pyCompany}}</p>
      <p>Status: {{lead.pyStatus}}</p>
      <p>Score: {{lead.pyLeadScore}}</p>
      <p>Value: ${{lead.pyPotentialValue}}</p>
    </div>
  </pega:foreach>
</pega:reference>
```

### 4. Lead Distribution (Manager Only)

#### Connector Configuration

**Connector Name**: `LeadManagementDistributeLeads`
**Method**: POST
**URL**: `/leads/distribute`

#### Implementation

```java
// Check if user is manager
if (D_LeadManagementAuth.pyRole = "SALES_MANAGER") {
    // Call distribution endpoint
    // Handle response
    pyDistributionResult = {{.data}}
} else {
    // Show error message
    pyErrorMessage = "Access denied. Manager role required."
}
```

### 5. Lead Escalation (Sales Person)

#### Connector Configuration

**Connector Name**: `LeadManagementEscalateLead`
**Method**: POST
**URL**: `/leads/{{.pyLeadID}}/escalate`

#### Implementation

```java
// Check if lead can be escalated
if (pyPotentialValue >= 1000000) {
    // Call escalation endpoint
    // Update lead status
    pyStatus = {{.data.status}}
    pyMessage = "Lead escalated successfully"
} else {
    pyErrorMessage = "Lead does not meet escalation criteria"
}
```

### 6. Lead Approval (Manager)

#### Connector Configuration

**Connector Name**: `LeadManagementApproveLead`
**Method**: POST
**URL**: `/leads/{{.pyLeadID}}/approve`

#### Implementation

```java
// Check if user is manager
if (D_LeadManagementAuth.pyRole = "SALES_MANAGER") {
    // Call approval endpoint
    pyStatus = {{.data.status}}
    pyMessage = "Lead approved successfully"
} else {
    pyErrorMessage = "Access denied. Manager role required."
}
```

### 7. Adding Comments to Lead

#### Connector Configuration

**Connector Name**: `LeadManagementAddComment`
**Method**: POST
**URL**: `/leads/{{.pyLeadID}}/history/comments`

#### Request Mapping

```java
{
  "commentText": "{{.pyCommentText}}",
  "action": "{{.pyAction}}"
}
```

#### Implementation

```java
// Add comment to lead
// Call add comment endpoint
pyCommentAdded = true
pyMessage = "Comment added successfully"
```

## Error Handling

### API Error Response Format

All API errors return a consistent format:

```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "error": "Detailed error message"
}
```

### Error Handling in Pega

#### 1. HTTP Status Code Handling

```java
// Handle different HTTP status codes
if (ConnectorResponse.StatusCode = 400) {
    pyErrorMessage = "Bad Request: " + {{.message}}
} else if (ConnectorResponse.StatusCode = 401) {
    pyErrorMessage = "Unauthorized: Please login again"
    // Trigger token refresh
} else if (ConnectorResponse.StatusCode = 403) {
    pyErrorMessage = "Access Denied: " + {{.message}}
} else if (ConnectorResponse.StatusCode = 404) {
    pyErrorMessage = "Resource Not Found: " + {{.message}}
} else if (ConnectorResponse.StatusCode = 500) {
    pyErrorMessage = "Server Error: Please try again later"
}
```

#### 2. Validation Error Handling

```java
// Handle validation errors
if ({{.success}} = false) {
    pyErrorMessage = {{.message}}
    pyDetailedError = {{.error}}
    
    // Show error to user
    pyShowError = true
}
```

#### 3. Network Error Handling

```java
// Handle network errors
if (ConnectorResponse.HasError = true) {
    pyErrorMessage = "Network Error: " + ConnectorResponse.ErrorMessage
    pyRetryCount = pyRetryCount + 1
    
    if (pyRetryCount < 3) {
        // Retry the operation
        pyRetryOperation = true
    } else {
        pyShowError = true
    }
}
```

### Error Display in UI

```html
<!-- Error Message Section -->
<pega:if test="pyShowError = true">
  <div class="alert alert-danger">
    <h4>Error</h4>
    <p>{{pyErrorMessage}}</p>
    <pega:if test="pyDetailedError != null">
      <p><small>{{pyDetailedError}}</small></p>
    </pega:if>
  </div>
</pega:if>
```

## Testing

### 1. Postman Collection

Import the provided Postman collection for API testing:

**Collection File**: `postman/Lead-Management-API.postman_collection.json`
**Environment File**: `postman/Lead-Management-API.postman_environment.json`

### 2. Pega Testing

#### Unit Testing

Create unit tests for each connector:

```java
// Test lead creation
@Test
public void testCreateLead() {
    // Setup test data
    pyLeadName = "Test Lead"
    pyCompany = "Test Corp"
    pyEmail = "test@testcorp.com"
    
    // Call connector
    // Assert response
    assert pyLeadID != null
    assert pyStatus = "NEW"
}
```

#### Integration Testing

Test complete workflows:

1. **Login Flow**: Test authentication and token retrieval
2. **Lead Creation**: Test lead creation from Pega case
3. **Lead Management**: Test lead updates and status changes
4. **Workflow Operations**: Test escalation and approval processes
5. **Error Handling**: Test error scenarios and recovery

### 3. Load Testing

Test API performance under load:

- **Concurrent Users**: 100+ simultaneous users
- **Request Rate**: 1000+ requests per minute
- **Response Time**: < 2 seconds for 95% of requests
- **Error Rate**: < 1% error rate

## Best Practices

### 1. Security

- **HTTPS Only**: Always use HTTPS in production
- **Token Management**: Implement secure token storage and rotation
- **Input Validation**: Validate all inputs before sending to API
- **Error Logging**: Log errors without exposing sensitive information

### 2. Performance

- **Caching**: Cache frequently accessed data
- **Pagination**: Use pagination for large data sets
- **Async Operations**: Use async operations for long-running tasks
- **Connection Pooling**: Configure appropriate connection pool settings

### 3. Error Handling

- **Graceful Degradation**: Handle API failures gracefully
- **User Feedback**: Provide clear error messages to users
- **Retry Logic**: Implement retry logic for transient failures
- **Monitoring**: Monitor API health and performance

### 4. Data Management

- **Data Synchronization**: Keep Pega data in sync with API
- **Data Validation**: Validate data before and after API calls
- **Data Mapping**: Use consistent data mapping patterns
- **Data Cleanup**: Clean up temporary data appropriately

### 5. Development

- **Versioning**: Use API versioning for backward compatibility
- **Documentation**: Keep integration documentation up to date
- **Testing**: Implement comprehensive testing strategy
- **Monitoring**: Monitor integration health and performance

## Troubleshooting

### Common Issues

#### 1. Authentication Failures

**Problem**: 401 Unauthorized errors
**Solution**: 
- Check token expiry
- Verify username/password
- Ensure token is included in Authorization header

#### 2. Permission Denied

**Problem**: 403 Forbidden errors
**Solution**:
- Verify user role
- Check endpoint permissions
- Ensure user has required access

#### 3. Data Mapping Issues

**Problem**: Incorrect data in Pega
**Solution**:
- Verify property mapping
- Check data types
- Validate API response format

#### 4. Performance Issues

**Problem**: Slow API responses
**Solution**:
- Check network connectivity
- Verify server performance
- Implement caching
- Optimize queries

### Debugging Tips

1. **Enable Logging**: Enable detailed logging for connectors
2. **Check Network**: Verify network connectivity and firewall settings
3. **Validate Data**: Check data format and validation rules
4. **Monitor Performance**: Use Pega monitoring tools
5. **Test Incrementally**: Test each integration point separately

## Support

For technical support or questions:

- **Email**: support@mig.com
- **Documentation**: https://docs.mig.com/lead-management-api
- **Issues**: https://github.com/mig/lead-management-service/issues
- **Pega Community**: https://community.pega.com

