#!/bin/bash

# Docker helper script for River City Resources

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to display usage
usage() {
    echo "Usage: $0 {build|up|down|restart|logs|shell|mysql|clean}"
    echo ""
    echo "Commands:"
    echo "  build    - Build Docker images"
    echo "  up       - Start all services"
    echo "  down     - Stop all services"
    echo "  restart  - Restart all services"
    echo "  logs     - Show logs (optional: service name)"
    echo "  shell    - Access Grails app shell"
    echo "  mysql    - Access MySQL shell"
    echo "  clean    - Remove all containers and volumes"
    exit 1
}

# Check if .env file exists
if [ ! -f .env ]; then
    echo -e "${YELLOW}Warning: .env file not found. Creating from .env.example...${NC}"
    cp .env.example .env
    echo -e "${GREEN}Created .env file. Please update with your settings.${NC}"
fi

case "$1" in
    build)
        echo -e "${GREEN}Building Docker images...${NC}"
        docker-compose build
        ;;
    up)
        echo -e "${GREEN}Starting services...${NC}"
        docker-compose up -d
        echo -e "${GREEN}Services started. Application available at http://localhost:8080${NC}"
        ;;
    down)
        echo -e "${YELLOW}Stopping services...${NC}"
        docker-compose down
        ;;
    restart)
        echo -e "${YELLOW}Restarting services...${NC}"
        docker-compose restart
        ;;
    logs)
        if [ -z "$2" ]; then
            docker-compose logs -f
        else
            docker-compose logs -f "$2"
        fi
        ;;
    shell)
        echo -e "${GREEN}Accessing Grails app shell...${NC}"
        docker-compose exec grails-app /bin/bash
        ;;
    mysql)
        echo -e "${GREEN}Accessing MySQL shell...${NC}"
        docker-compose exec mysql mysql -u grails -p
        ;;
    clean)
        echo -e "${RED}This will remove all containers and volumes. Are you sure? (y/N)${NC}"
        read -r response
        if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
            docker-compose down -v
            echo -e "${GREEN}Cleanup complete.${NC}"
        else
            echo -e "${YELLOW}Cleanup cancelled.${NC}"
        fi
        ;;
    *)
        usage
        ;;
esac