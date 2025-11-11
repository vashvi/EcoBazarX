@echo off
REM Quick Setup Script for EcoBazaarX Frontend-Backend Integration
REM Run this script to set up both frontend and backend

echo.
echo 🚀 EcoBazaarX Setup Script
echo ==========================
echo.

REM Check prerequisites
echo 📋 Checking Prerequisites...
echo.

REM Check Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java not found. Please install JDK 21
    pause
    exit /b 1
)
echo ✅ Java found

REM Check Maven
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven not found. Please install Maven
    pause
    exit /b 1
)
echo ✅ Maven found

REM Check Node
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Node not found. Please install Node.js
    pause
    exit /b 1
)
echo ✅ Node found

REM Check npm
npm -v >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ npm not found
    pause
    exit /b 1
)
echo ✅ npm found

echo.
echo 🔧 Setting up Backend...
cd Infosys
echo Installing backend dependencies...
call mvnw.cmd clean install
if %errorlevel% neq 0 (
    echo ❌ Backend setup failed
    pause
    exit /b 1
)
echo ✅ Backend dependencies installed

echo.
echo 🎨 Setting up Frontend...
cd ..\frontend
echo Installing frontend dependencies...
call npm install --silent
if %errorlevel% neq 0 (
    echo ❌ Frontend setup failed
    pause
    exit /b 1
)
echo ✅ Frontend dependencies installed

echo.
echo ✨ Setup Complete!
echo.
echo 📝 Next Steps:
echo.
echo 1. Open Command Prompt/PowerShell Terminal 1:
echo    cd Infosys
echo    mvnw.cmd spring-boot:run
echo.
echo 2. Open Command Prompt/PowerShell Terminal 2:
echo    cd frontend
echo    npm run dev
echo.
echo 3. Frontend will open at http://localhost:3000
echo 4. Backend will be available at http://localhost:8080
echo.
pause
