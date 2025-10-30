# Lead Management Service - Docker Setup

This guide explains how to run the Lead Management Service using Docker and Docker Compose.

## Prerequisites

- Docker Desktop (version 20.10 or higher)
- Docker Compose (version 2.0 or higher)
- At least 4GB of available RAM
- Ports 8080, 5432, and 5050 available on your system

## Quick Start

### 1. Build and Run with Docker Compose

Navigate to the lead-management-service directory and run:

```bash
cd casemanagement-api/lead-management-service
docker-compose up --build
```

This command will:
- Build the Lead Management Service Docker image
- Start PostgreSQL database
- Initialize the database with the required schema
- Start the Lead Management Service
- Start pgAdmin for database management

### 2. Access the Services

- **Lead Management API**: http://localhost:8080/api
- **API Documentation (Swagger)**: http://localhost:8080/api/swagger-ui.html
- **Health Check**: http://localhost:8080/api/actuator/health
- **pgAdmin**: http://localhost:5050 (admin@mig.com / admin)

### 3. Stop the Services

```bash
docker-compose down
```

To also remove volumes (database data):

```bash
docker-compose down -v
```

## Individual Service Commands

### Build Only the Application

```bash
docker build -t lead-management-service .
```

### Run PostgreSQL Only

```bash
docker-compose up postgres
```

### Run Application Only (requires PostgreSQL to be running)

```bash
docker-compose up lead-management-service
```

## Database Management

### Connect to PostgreSQL

```bash
docker exec -it lead-management-postgres psql -U lead_mgmt_user -d lead_management
```

### View Database Logs

```bash
docker logs lead-management-postgres
```

### Reset Database

```bash
docker-compose down -v
docker-compose up postgres
```

## Application Management

### View Application Logs

```bash
docker logs lead-management-service
```

### Follow Logs in Real-time

```bash
docker logs -f lead-management-service
```

### Restart Application

```bash
docker-compose restart lead-management-service
```

## Environment Variables

You can customize the application behavior using environment variables:

| Variable | Default Value | Description |
|----------|---------------|-------------|
| `SPRING_PROFILES_ACTIVE` | `docker` | Spring profile to use |
| `DB_URL` | `jdbc:postgresql://postgres:5432/lead_management` | Database connection URL |
| `DB_USERNAME` | `lead_mgmt_user` | Database username |
| `DB_PASSWORD` | `password` | Database password |
| `JWT_SECRET` | `mySecretKey123456789012345678901234567890` | JWT secret key |

### Example: Custom Environment Variables

Create a `.env` file in the lead-management-service directory:

```env
DB_PASSWORD=mySecurePassword
JWT_SECRET=myVerySecureJWTSecretKey123456789012345678901234567890
```

Then run:

```bash
docker-compose --env-file .env up
```

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   - Check if ports 8080, 5432, or 5050 are already in use
   - Stop conflicting services or change ports in docker-compose.yml

2. **Database Connection Failed**
   - Ensure PostgreSQL container is healthy: `docker-compose ps`
   - Check database logs: `docker logs lead-management-postgres`
   - Wait for database initialization to complete

3. **Application Won't Start**
   - Check application logs: `docker logs lead-management-service`
   - Ensure all environment variables are set correctly
   - Verify database is accessible from the application container

4. **Out of Memory**
   - Increase Docker Desktop memory allocation
   - Check system resources: `docker stats`

### Health Checks

The application includes health checks that verify:
- Application is responding to HTTP requests
- Database connection is working
- All required services are running

Check health status:

```bash
curl http://localhost:8080/api/actuator/health
```

### Debug Mode

To run in debug mode with more verbose logging:

```bash
docker-compose -f docker-compose.yml -f docker-compose.debug.yml up
```

## Development

### Hot Reload (Development)

For development with hot reload, you can mount the source code:

```yaml
# Add to docker-compose.yml under lead-management-service
volumes:
  - ./src:/app/src
```

### Running Tests

```bash
docker-compose exec lead-management-service ./mvnw test
```

### Database Migrations

The database schema is automatically initialized when PostgreSQL starts for the first time. The schema file is located at `../documentation/pega_postgresql_ddl.sql`.

## Production Deployment

For production deployment:

1. Change default passwords and secrets
2. Use environment variables for sensitive data
3. Enable SSL/TLS
4. Configure proper logging
5. Set up monitoring and alerting
6. Use a managed database service

### Production Environment Variables

```env
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:postgresql://your-prod-db:5432/lead_management
DB_USERNAME=your_prod_user
DB_PASSWORD=your_secure_password
JWT_SECRET=your_very_secure_jwt_secret
```

## Support

For issues or questions:
1. Check the logs first
2. Review this documentation
3. Check the main project README
4. Contact the development team
