#!/bin/bash

# Define colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to handle cleanup on exit (Ctrl+C)
cleanup() {
    echo -e "\n${RED}Stopping all services...${NC}"
    
    if [ -n "$BACKEND_PID" ]; then
        echo "Killing Backend (PID: $BACKEND_PID)..."
        kill $BACKEND_PID 2>/dev/null
    fi
    
    if [ -n "$FRONTEND_PID" ]; then
        echo "Killing Frontend (PID: $FRONTEND_PID)..."
        kill $FRONTEND_PID 2>/dev/null
    fi
    
    echo -e "${GREEN}All services stopped.${NC}"
    exit
}

# Trap SIGINT (Ctrl+C) and call cleanup
trap cleanup SIGINT

echo -e "${BLUE}=== Starting Barber Shop Project (Interactive Mode) ===${NC}"

# 1. Start Docker Container (Database)
echo -e "${GREEN}[1/3] Checking Database...${NC}"
if [ ! "$(docker ps -q -f name=barbearia-container)" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=barbearia-container)" ]; then
        echo "Starting existing barbearia-container..."
        docker start barbearia-container
    else
        echo "Creating and starting new barbearia-container..."
        docker run --name barbearia-container -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=barbearia_db -p 3306:3306 -v barbearia_db_data:/var/lib/mysql -d mysql:8.0
    fi
else
    echo "Database container is already running."
fi

# Wait for DB to be ready
echo "Waiting for database to initialize..."
sleep 3

# 2. Start Backend
echo -e "${GREEN}[2/3] Starting Backend (Spring Boot)...${NC}"
cd barbearia-backend || { echo -e "${RED}Error: Backend directory not found!${NC}"; exit 1; }

# Run Backend in background but keep output attached to this shell
./mvnw spring-boot:run -pl dominio-principal -DskipTests &
BACKEND_PID=$!
echo "Backend started with PID $BACKEND_PID"

cd ..

# 3. Start Frontend
echo -e "${GREEN}[3/3] Starting Frontend (React/Vite)...${NC}"
cd apresentacao-frontend || { echo -e "${RED}Error: Frontend directory not found!${NC}"; exit 1; }

# Run Frontend in background but keep output attached to this shell
npm run dev &
FRONTEND_PID=$!
echo "Frontend started with PID $FRONTEND_PID"

cd ..

echo -e "${BLUE}=== Project Running! Press Ctrl+C to stop ===${NC}"
echo -e "Backend:  http://localhost:8080"
echo -e "Frontend: http://localhost:5173"

# Wait for both processes
wait $BACKEND_PID $FRONTEND_PID
