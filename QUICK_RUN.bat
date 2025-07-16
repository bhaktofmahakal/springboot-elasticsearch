@echo off
echo =========================================
echo   Course Search Application - Quick Run
echo =========================================
echo.

echo Step 1: Starting Elasticsearch...
docker-compose up -d

echo.
echo Step 2: Waiting for Elasticsearch to start...
timeout /t 10 /nobreak > nul

echo.
echo Step 3: Starting Spring Boot Application...
mvn spring-boot:run

echo.
echo Application should be running on http://localhost:8080
echo.
echo Test with:
echo curl "http://localhost:8080/api/search"
echo.
pause