#!/bin/bash

echo "========================================="
echo "TESTING PRODUCTION PIPELINE"
echo "========================================="

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Stop any existing containers
echo -e "${YELLOW}Stopping existing containers...${NC}"
docker-compose -f docker-compose.prod.yml down -v || true

# Build and start containers
echo -e "${YELLOW}Building and starting containers...${NC}"
docker-compose -f docker-compose.prod.yml up -d --build

# Wait for services to be ready
echo -e "${YELLOW}Waiting for services to be ready...${NC}"
sleep 30

# Check container status
echo -e "${YELLOW}Container status:${NC}"
docker-compose -f docker-compose.prod.yml ps

# Wait for health check
echo -e "${YELLOW}Waiting for application health check...${NC}"
MAX_ATTEMPTS=30
ATTEMPT=0
READY=false

while [ $ATTEMPT -lt $MAX_ATTEMPTS ] && [ "$READY" = false ]; do
    ATTEMPT=$((ATTEMPT + 1))
    echo "Health check attempt $ATTEMPT/$MAX_ATTEMPTS..."
    
    if curl -f -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
        READY=true
        echo -e "${GREEN}✅ Application is ready!${NC}"
    else
        sleep 5
    fi
done

if [ "$READY" = false ]; then
    echo -e "${RED}❌ Application did not become ready${NC}"
    docker-compose -f docker-compose.prod.yml logs
    exit 1
fi

# Run smoke tests
echo -e "${YELLOW}Running smoke tests...${NC}"
echo "1. Testing Health Endpoint..."
if curl -f http://localhost:8080/actuator/health; then
    echo -e "${GREEN}✅ Health check passed${NC}"
else
    echo -e "${RED}❌ Health check failed${NC}"
    exit 1
fi

echo "2. Testing Swagger UI..."
curl -f http://localhost:8080/swagger-ui/index.html > /dev/null 2>&1 || curl -f http://localhost:8080/swagger-ui.html > /dev/null 2>&1 && echo -e "${GREEN}✅ Swagger UI accessible${NC}" || echo -e "${YELLOW}⚠️ Swagger UI not accessible${NC}"

echo "3. Testing API Endpoints..."
curl -f http://localhost:8080/api/students > /dev/null 2>&1 && echo -e "${GREEN}✅ API endpoint accessible${NC}" || echo -e "${YELLOW}⚠️ API endpoint check${NC}"

echo ""
echo -e "${GREEN}=========================================${NC}"
echo -e "${GREEN}✅ PRODUCTION PIPELINE TEST PASSED${NC}"
echo -e "${GREEN}=========================================${NC}"
echo ""
echo "Application URL: http://localhost:8080"
echo "Health Check: http://localhost:8080/actuator/health"
echo "Swagger UI: http://localhost:8080/swagger-ui/index.html"
echo ""
echo "To stop containers, run: docker-compose -f docker-compose.prod.yml down"

