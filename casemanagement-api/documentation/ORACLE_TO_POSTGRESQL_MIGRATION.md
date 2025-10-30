# Oracle to PostgreSQL Migration Guide

This document outlines the changes made to migrate the Lead Management Service from Oracle to PostgreSQL database.

## Overview

The migration involved converting the database schema, updating application configuration, and creating Docker-based deployment setup for PostgreSQL.

## Database Schema Changes

### 1. Data Types Conversion

| Oracle Type | PostgreSQL Type | Notes |
|-------------|-----------------|-------|
| `NUMBER` | `BIGINT` | For primary keys and integer fields |
| `NUMBER(12,2)` | `DECIMAL(12,2)` | For monetary values |
| `VARCHAR2(n)` | `VARCHAR(n)` | String fields |
| `CLOB` | `TEXT` | Large text fields |
| `DATE` | `TIMESTAMP` | Date/time fields |
| `CHAR(1)` | `CHAR(1)` | Single character fields |

### 2. Sequence Changes

**Oracle:**
```sql
CREATE SEQUENCE APP_USERS_SEQ START WITH 1 INCREMENT BY 1 NOCACHE;
```

**PostgreSQL:**
```sql
CREATE SEQUENCE app_users_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
```

### 3. Table and Column Naming

- Converted all table and column names to lowercase
- Maintained referential integrity with foreign keys
- Updated all constraints to use PostgreSQL syntax

### 4. Trigger Conversion

**Oracle Trigger:**
```sql
CREATE OR REPLACE TRIGGER TRG_USERS_UPDATE
    BEFORE UPDATE ON APP_USERS
    FOR EACH ROW
BEGIN
    :NEW.PX_UPDATED_DATETIME := SYSDATE;
END;
```

**PostgreSQL Trigger:**
```sql
CREATE OR REPLACE FUNCTION update_updated_datetime()
RETURNS TRIGGER AS $$
BEGIN
    NEW.px_updated_datetime = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_update
    BEFORE UPDATE ON app_users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_datetime();
```

### 5. View Updates

- Updated all views to use lowercase table and column names
- Maintained the same business logic and filtering
- Preserved all comments and documentation

## Application Configuration Changes

### 1. Maven Dependencies (pom.xml)

**Removed Oracle Dependencies:**
```xml
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc11</artifactId>
    <version>${oracle.version}</version>
</dependency>
<dependency>
    <groupId>com.oracle.database.ha</groupId>
    <artifactId>ons</artifactId>
    <version>${oracle.version}</version>
</dependency>
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ucp</artifactId>
    <version>${oracle.version}</version>
</dependency>
```

**Added PostgreSQL Dependency:**
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. Database Configuration (application.yml)

**Connection URL:**
- Oracle: `jdbc:oracle:thin:@localhost:1521:xe`
- PostgreSQL: `jdbc:postgresql://localhost:5432/lead_management`

**Driver Class:**
- Oracle: `oracle.jdbc.OracleDriver`
- PostgreSQL: `org.postgresql.Driver`

**Hibernate Dialect:**
- Oracle: `org.hibernate.dialect.Oracle12cDialect`
- PostgreSQL: `org.hibernate.dialect.PostgreSQLDialect`

### 3. Environment Profiles

Added a new `docker` profile for containerized deployment:
```yaml
spring:
  config:
    activate:
      on-profile: docker
  datasource:
    url: jdbc:postgresql://postgres:5432/lead_management
    username: ${DB_USERNAME:lead_mgmt_user}
    password: ${DB_PASSWORD:password}
```

## Docker Setup

### 1. Dockerfile

Created a multi-stage Dockerfile that:
- Uses OpenJDK 17 as base image
- Installs necessary packages
- Copies Maven wrapper and dependencies
- Builds the application
- Creates non-root user for security
- Includes health checks
- Exposes port 8080

### 2. Docker Compose

Created `docker-compose.yml` with:
- PostgreSQL 15 Alpine image
- Application service
- pgAdmin for database management
- Proper networking and health checks
- Volume persistence for database data
- Environment variable configuration

### 3. Database Initialization

The PostgreSQL container automatically initializes with:
- Database creation (`lead_management`)
- User creation (`lead_mgmt_user`)
- Schema creation from `pega_postgresql_ddl.sql`
- Sample data insertion

## Migration Benefits

### 1. Cost Reduction
- PostgreSQL is open-source and free
- No licensing costs for database software
- Lower infrastructure costs

### 2. Performance Improvements
- Better query optimization
- Improved indexing capabilities
- Enhanced JSON support for future features

### 3. Development Experience
- Better Docker integration
- Easier local development setup
- Simplified deployment process

### 4. Cloud Compatibility
- Better support in cloud platforms
- Easier container orchestration
- Improved scalability options

## Testing the Migration

### 1. Database Schema Validation

```sql
-- Check tables exist
\dt

-- Check sequences
\ds

-- Check views
\dv

-- Verify data
SELECT COUNT(*) FROM app_users;
SELECT COUNT(*) FROM leads;
SELECT COUNT(*) FROM lead_history;
```

### 2. Application Testing

1. Start the services:
   ```bash
   docker-compose up
   ```

2. Test API endpoints:
   ```bash
   curl http://localhost:8080/api/actuator/health
   ```

3. Verify database connectivity:
   ```bash
   curl http://localhost:8080/api/leads
   ```

### 3. Data Integrity Verification

- All foreign key relationships maintained
- Data types correctly converted
- Triggers functioning properly
- Views returning expected results

## Rollback Plan

If rollback to Oracle is needed:

1. Revert `pom.xml` to include Oracle dependencies
2. Update `application.yml` with Oracle configuration
3. Use the original `pega_oracle_ddl.sql` for database setup
4. Update Docker configuration for Oracle database

## Future Considerations

### 1. Performance Optimization
- Monitor query performance
- Add appropriate indexes based on usage patterns
- Consider partitioning for large tables

### 2. Feature Enhancements
- Leverage PostgreSQL JSON capabilities
- Implement full-text search
- Add advanced analytics features

### 3. Monitoring and Maintenance
- Set up database monitoring
- Implement backup strategies
- Plan for scaling requirements

## Files Modified

1. `pom.xml` - Updated dependencies
2. `application.yml` - Updated database configuration
3. `pega_postgresql_ddl.sql` - New PostgreSQL schema
4. `Dockerfile` - New container configuration
5. `docker-compose.yml` - New orchestration setup
6. `DOCKER_README.md` - Docker usage documentation

## Support

For questions or issues related to the migration:
1. Check the Docker logs: `docker logs lead-management-service`
2. Verify database connectivity: `docker logs lead-management-postgres`
3. Review this migration guide
4. Contact the development team
