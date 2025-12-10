@echo off
REM Build AI Service Docker Image

echo Building AI Service Docker Image...

cd /d "%~dp0"

docker build -t aiservice:latest .

if %ERRORLEVEL% EQU 0 (
    echo ✅ AI Service image built successfully!
    echo Image: aiservice:latest
    echo.
    echo To run the service:
    echo   docker-compose up -d
    echo.
    echo To test the service:
    echo   curl http://localhost:8086/health
) else (
    echo ❌ Build failed!
    exit /b 1
)
