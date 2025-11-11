#!/bin/bash
# Quick Setup Script for EcoBazaarX Frontend-Backend Integration
# Run this script to set up both frontend and backend

echo "🚀 EcoBazaarX Setup Script"
echo "=========================="

# Check prerequisites
echo ""
echo "📋 Checking Prerequisites..."

# Check Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[0-9]+')
    echo "✅ Java $JAVA_VERSION found"
else
    echo "❌ Java not found. Please install JDK 21"
    exit 1
fi

# Check Maven
if command -v mvn &> /dev/null; then
    echo "✅ Maven found"
else
    echo "❌ Maven not found. Please install Maven"
    exit 1
fi

# Check Node
if command -v node &> /dev/null; then
    NODE_VERSION=$(node -v)
    echo "✅ Node $NODE_VERSION found"
else
    echo "❌ Node not found. Please install Node.js"
    exit 1
fi

# Check npm
if command -v npm &> /dev/null; then
    NPM_VERSION=$(npm -v)
    echo "✅ npm $NPM_VERSION found"
else
    echo "❌ npm not found"
    exit 1
fi

echo ""
echo "🔧 Setting up Backend..."
cd Infosys
echo "Installing dependencies..."
mvn clean install -q
if [ $? -eq 0 ]; then
    echo "✅ Backend dependencies installed"
else
    echo "❌ Backend setup failed"
    exit 1
fi

echo ""
echo "🎨 Setting up Frontend..."
cd ../frontend
echo "Installing dependencies..."
npm install --silent
if [ $? -eq 0 ]; then
    echo "✅ Frontend dependencies installed"
else
    echo "❌ Frontend setup failed"
    exit 1
fi

echo ""
echo "✨ Setup Complete!"
echo ""
echo "📝 Next Steps:"
echo "1. Open Terminal 1:"
echo "   cd Infosys"
echo "   mvnw spring-boot:run"
echo ""
echo "2. Open Terminal 2:"
echo "   cd frontend"
echo "   npm run dev"
echo ""
echo "3. Frontend will open at http://localhost:3000"
echo "4. Backend will be available at http://localhost:8080"
echo ""
