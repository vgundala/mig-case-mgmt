@echo off
echo Building Lead Management Service Docker Image...
echo.

REM Check if Docker is running
docker version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not running. Please start Docker Desktop and try again.
    pause
    exit /b 1
)

echo Docker is running. Building image...
echo.

REM Build the Docker image
docker build -t lead-management-service .

if %errorlevel% equ 0 (
    echo.
    echo SUCCESS: Docker image built successfully!
    echo Image name: lead-management-service
    echo.
    echo To run the complete stack with database:
    echo   docker-compose up
    echo.
    echo To run only the application (requires database):
    echo   docker-compose up lead-management-service
    echo.
) else (
    echo.
    echo ERROR: Docker build failed!
    echo Please check the error messages above.
    echo.
)

pause
