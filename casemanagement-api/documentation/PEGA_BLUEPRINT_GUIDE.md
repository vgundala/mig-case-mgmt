# Pega Blueprint Guide - Lead Management System

## Overview

This guide provides comprehensive documentation for implementing the Lead Management System in Pega Blueprint. The system manages the complete sales workflow from lead generation to client conversion for Mega Investment Group.

## System Architecture

The modernized system consists of:
- **Pega Platform**: Handles workflows, UI, and business rules
- **Spring Boot Microservice**: Provides data access and business logic
- **Oracle Database**: Stores lead and user data
- **JWT Authentication**: Secures API communication

## Personas

### 1. Sales Person
**Role**: Individual contributor responsible for managing assigned leads

**Responsibilities**:
- Review assigned leads
- Nurture leads through phone calls, emails, and meetings
- Add comments and track activities
- Request approval for standard leads (< $1M)
- Escalate high-value leads (≥ $1M) to manager

**Permissions**:
- View assigned leads only
- Update lead information and status
- Add comments and activities
- Request approvals
- Escalate leads

**Key Workflows**:
- Lead Review Process
- Lead Nurturing Process
- Standard Approval Request Process
- Lead Escalation Process

### 2. Sales Manager
**Role**: Team leader responsible for lead distribution and high-value lead management

**Responsibilities**:
- View all leads in the system
- Distribute new leads to sales team
- Handle escalated high-value leads
- Approve or reject lead conversions
- Monitor team performance

**Permissions**:
- View all leads
- Distribute leads to sales team
- Handle escalated leads
- Approve/reject conversions
- Access reporting and analytics

**Key Workflows**:
- Lead Distribution Process
- High-Value Lead Management Process
- Lead Approval Process
- Team Performance Monitoring

## Data Model Mapping

### Oracle Tables → Pega Data Classes

#### 1. APP_USERS → Data-User
```yaml
Data Class: Data-User
Properties:
  pyUserID: String (Primary Key) → USER_ID
  pyUserName: String → USERNAME
  pyPassword: String → PASSWORD (hashed)
  pyRole: String → ROLE (SALES_PERSON/SALES_MANAGER)
  pyFirstName: String → FIRST_NAME
  pyLastName: String → LAST_NAME
  pyEmail: String → EMAIL
  pyPhone: String → PHONE
  pyIsActive: Boolean → IS_ACTIVE
  pyCreatedDate: DateTime → CREATED_DATE
  pyLastLoginDate: DateTime → LAST_LOGIN_DATE
  pyCreatedBy: String → PX_CREATED_BY
  pyUpdatedBy: String → PX_UPDATED_BY
  pyCreatedDateTime: DateTime → PX_CREATED_DATETIME
  pyUpdatedDateTime: DateTime → PX_UPDATED_DATETIME
```

#### 2. LEADS → Data-Lead
```yaml
Data Class: Data-Lead
Properties:
  pyLeadID: String (Primary Key) → LEAD_ID
  pyLeadName: String → LEAD_NAME
  pyCompany: String → COMPANY
  pyEmail: String → EMAIL
  pyPhone: String → PHONE
  pyStatus: String → STATUS (NEW/ASSIGNED/IN_PROGRESS/PRE_CONVERSION/CONVERTED/REJECTED)
  pyAssignedTo: String → ASSIGNED_TO (Reference to Data-User)
  pyPotentialValue: Decimal → POTENTIAL_VALUE
  pyLeadSource: String → LEAD_SOURCE
  pyLeadScore: Integer → LEAD_SCORE (0-100)
  pyCreatedDate: DateTime → CREATED_DATE
  pyUpdatedDate: DateTime → UPDATED_DATE
  pyDescription: String → DESCRIPTION
  pyIndustry: String → INDUSTRY
  pyCompanySize: String → COMPANY_SIZE
  pyLocation: String → LOCATION
  pyCreatedBy: String → PX_CREATED_BY
  pyUpdatedBy: String → PX_UPDATED_BY
  pyCreatedDateTime: DateTime → PX_CREATED_DATETIME
  pyUpdatedDateTime: DateTime → PX_UPDATED_DATETIME
```

#### 3. LEAD_HISTORY → Data-LeadHistory
```yaml
Data Class: Data-LeadHistory
Properties:
  pyHistoryID: String (Primary Key) → HISTORY_ID
  pyLeadID: String → LEAD_ID (Reference to Data-Lead)
  pyUserID: String → USER_ID (Reference to Data-User)
  pyCommentText: String → COMMENT_TEXT
  pyAction: String → ACTION
  pyTimestamp: DateTime → TIMESTAMP
  pyActionType: String → ACTION_TYPE (SYSTEM/USER_ACTION/WORKFLOW)
  pyOldStatus: String → OLD_STATUS
  pyNewStatus: String → NEW_STATUS
  pyCreatedBy: String → PX_CREATED_BY
  pyUpdatedBy: String → PX_UPDATED_BY
  pyCreatedDateTime: DateTime → PX_CREATED_DATETIME
  pyUpdatedDateTime: DateTime → PX_UPDATED_DATETIME
```

## Business Rules

### 1. Lead Scoring Algorithm
```yaml
Rule Name: CalculateLeadScore
Description: Calculates lead score based on multiple criteria
Logic:
  - Potential Value:
    - > $500K: +50 points
    - $100K-$500K: +20 points
    - < $100K: +5 points
  - Lead Source:
    - Partner Referral: +30 points
    - Webinar: +15 points
    - Website Signup: +10 points
    - Cold Call: +5 points
  - Data Completeness:
    - Both email and phone: +15 points
    - Either email or phone: +5 points
    - Neither: +0 points
```

### 2. Lead Distribution Algorithm
```yaml
Rule Name: DistributeLeads
Description: Round-robin distribution of new leads to sales team
Logic:
  - Get all leads with status = 'NEW'
  - Get all active users with role = 'SALES_PERSON'
  - Assign leads in round-robin fashion
  - Update lead status to 'ASSIGNED'
  - Log distribution activity in lead history
```

### 3. Escalation Criteria
```yaml
Rule Name: EscalateHighValueLeads
Description: Escalates leads with potential value >= $1M
Logic:
  - Check if lead.potentialValue >= 1000000
  - Change status to 'PRE_CONVERSION'
  - Assign to Sales Manager
  - Log escalation in lead history
```

### 4. Status Transition Rules
```yaml
Valid Status Transitions:
  NEW → ASSIGNED (via distribution)
  ASSIGNED → IN_PROGRESS (when sales person starts working)
  IN_PROGRESS → PRE_CONVERSION (when escalated)
  IN_PROGRESS → CONVERTED (when approved)
  IN_PROGRESS → REJECTED (when rejected)
  PRE_CONVERSION → CONVERTED (when manager approves)
  PRE_CONVERSION → REJECTED (when manager rejects)
```

## Workflow Summary

### 1. Lead Creation Workflow
**Trigger**: New lead entry
**Process**:
1. System creates lead with status 'NEW'
2. System calculates lead score automatically
3. Lead appears in manager's new leads dashboard

### 2. Lead Distribution Workflow
**Trigger**: Manager clicks "Distribute Leads"
**Process**:
1. System identifies all 'NEW' leads
2. System identifies all active sales persons
3. System distributes leads using round-robin algorithm
4. System updates lead status to 'ASSIGNED'
5. System logs distribution activity

### 3. Lead Nurturing Workflow
**Trigger**: Sales person receives assigned lead
**Process**:
1. Sales person reviews lead details
2. Sales person adds comments and tracks activities
3. System updates lead status to 'IN_PROGRESS'
4. System logs all activities in lead history

### 4. Lead Conversion Workflow
**Two Paths Based on Lead Value**:

#### Standard Lead (< $1M):
1. Sales person requests approval
2. Manager reviews and approves/rejects
3. System updates status to 'CONVERTED' or 'REJECTED'

#### High-Value Lead (≥ $1M):
1. Sales person escalates to manager
2. System updates status to 'PRE_CONVERSION'
3. Manager handles the lead directly
4. Manager approves or rejects conversion

## Integration Points

### 1. Authentication
- **Endpoint**: `POST /api/auth/login`
- **Purpose**: Authenticate users and receive JWT token
- **Usage**: Initial login, token refresh

### 2. Lead Management
- **Endpoints**:
  - `GET /api/leads` - List all leads
  - `GET /api/leads/my-leads` - Get user's assigned leads
  - `POST /api/leads` - Create new lead
  - `PUT /api/leads/{id}` - Update lead
  - `GET /api/leads/{id}` - Get lead details

### 3. Workflow Operations
- **Endpoints**:
  - `POST /api/leads/distribute` - Distribute leads (Manager only)
  - `POST /api/leads/{id}/escalate` - Escalate lead (Sales Person)
  - `POST /api/leads/{id}/approve` - Approve lead (Manager)
  - `PUT /api/leads/{id}/status` - Update lead status

### 4. Lead History
- **Endpoints**:
  - `GET /api/leads/{id}/history` - Get lead history
  - `POST /api/leads/{id}/comments` - Add comment

## UI Components

### 1. Dashboard Components
- **My Leads Dashboard**: Shows assigned leads for sales persons
- **New Leads Dashboard**: Shows unassigned leads for managers
- **High-Value Leads Dashboard**: Shows leads requiring special attention

### 2. Form Components
- **Lead Creation Form**: Create new leads
- **Lead Details Form**: View and edit lead information
- **Comment Form**: Add comments and activities
- **User Profile Form**: Manage user information

### 3. List Components
- **Lead List**: Displays leads with filtering and sorting
- **Activity Timeline**: Shows lead history chronologically
- **User List**: Shows team members (for managers)

### 4. Action Components
- **Distribute Leads Button**: Triggers lead distribution
- **Escalate Lead Button**: Escalates high-value leads
- **Approve/Reject Buttons**: For lead conversion decisions
- **Add Comment Button**: Adds new activities

## Security Considerations

### 1. Authentication
- JWT tokens with configurable expiration
- Secure password hashing using BCrypt
- Session management for web interface

### 2. Authorization
- Role-based access control
- Endpoint-level permissions
- Data access restrictions based on user role

### 3. Data Protection
- HTTPS for all communications
- Input validation and sanitization
- SQL injection prevention
- XSS protection

## Performance Considerations

### 1. Database Optimization
- Indexes on frequently queried columns
- Connection pooling
- Query optimization

### 2. Caching Strategy
- User data caching
- Lead list caching
- Configuration caching

### 3. API Optimization
- Pagination for large datasets
- Response compression
- Efficient data serialization

## Monitoring and Logging

### 1. Application Logging
- Structured logging with correlation IDs
- Error tracking and alerting
- Performance metrics

### 2. Business Metrics
- Lead conversion rates
- Sales team performance
- Lead source effectiveness
- Average lead processing time

## Testing Strategy

### 1. Unit Testing
- Service layer testing
- Business rule validation
- Data access testing

### 2. Integration Testing
- API endpoint testing
- Database integration testing
- Authentication testing

### 3. User Acceptance Testing
- Workflow testing
- UI component testing
- End-to-end scenario testing

## Deployment Considerations

### 1. Environment Setup
- Development environment
- Testing environment
- Production environment

### 2. Configuration Management
- Environment-specific configurations
- Secret management
- Database connection strings

### 3. Backup and Recovery
- Database backup strategy
- Application data backup
- Disaster recovery procedures

## Future Enhancements

### 1. Advanced Analytics
- Lead scoring machine learning models
- Predictive analytics for lead conversion
- Advanced reporting and dashboards

### 2. Integration Capabilities
- CRM system integration
- Email marketing integration
- Calendar integration

### 3. Mobile Support
- Mobile-responsive UI
- Mobile app development
- Offline capability

## Support and Maintenance

### 1. Documentation
- User manuals
- Administrator guides
- API documentation

### 2. Training
- End-user training
- Administrator training
- Developer training

### 3. Support Process
- Issue reporting
- Escalation procedures
- Maintenance windows

