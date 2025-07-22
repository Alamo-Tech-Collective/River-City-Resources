# River City Resources - Docker Setup

This directory contains Docker configuration for running the River City Resources application.

## Quick Start

1. Copy the environment file:
   ```bash
   cp .env.example .env
   ```

2. Build and start the services:
   ```bash
   docker-compose up -d --build
   ```

3. Access the application at http://localhost:8080

## Services

### MySQL Database
- Internal port: 3306 (not exposed externally)
- Database name: rivercityresources
- Default credentials are in `.env.example`

### Grails Application
- Exposed port: 8080 (configurable via APP_PORT in .env)
- Health check endpoint: /actuator/health

## Common Commands

```bash
# View logs
docker-compose logs -f grails-app

# Stop services
docker-compose down

# Stop and remove volumes (destroys database)
docker-compose down -v

# Rebuild after code changes
docker-compose up -d --build grails-app

# Access MySQL CLI
docker-compose exec mysql mysql -u grails -p

# Run Grails commands
docker-compose exec grails-app ./gradlew <command>
```

## Production Deployment

1. Update `.env` with secure passwords
2. Configure your reverse proxy to forward to port 8080
3. Consider using Docker Swarm or Kubernetes for orchestration
4. Set up regular database backups

## Troubleshooting

- If the app fails to start, check MySQL is healthy: `docker-compose ps`
- View detailed logs: `docker-compose logs grails-app`
- Ensure no other services are using port 8080