# Frontend-Backend Integration Guide

## Overview
This document provides setup and connection instructions for integrating the **EcoBazaarX** React frontend with the Spring Boot backend.

---

## Architecture

### Frontend
- **Framework**: React with Vite
- **Port**: 3000
- **Location**: `/frontend`
- **API Base URL**: `http://localhost:8080/api` (proxied through Vite)

### Backend
- **Framework**: Spring Boot 3.5.7
- **Port**: 8080
- **Location**: `/Infosys`
- **Java Version**: 21
- **Build Tool**: Maven

---

## Prerequisites

### System Requirements
- **Node.js**: v16 or higher (for frontend)
- **Java**: JDK 21 (for backend)
- **Maven**: 3.8.9 or higher
- **PostgreSQL**: v14+ (for database)
- **Git**: for version control

### Installation Steps

#### 1. Java & Maven
```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

#### 2. Node.js & npm
```bash
# Check Node version
node --version

# Check npm version
npm --version
```

#### 3. PostgreSQL Database
```bash
# Create database for EcoBazaarX
createdb -U postgres EcoBazarX_db

# Or using psql
psql -U postgres -c "CREATE DATABASE EcoBazarX_db;"
```

---

## Setup Instructions

### Backend Setup (Spring Boot)

#### 1. Navigate to Backend Directory
```bash
cd Infosys
```

#### 2. Update Database Configuration
Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/EcoBazarX_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_DB_PASSWORD

# JWT Configuration
JWT_SECRET=U2VjdXJlS2V5Rm9ySm9obkRvZTEyMzQ1Njc4OQ==
JWT_EXPIRATION=108000000

# Server Port
server.port=8080
```

#### 3. Build Backend
```bash
# Using Maven wrapper
mvnw clean install

# Or with Maven
mvn clean install
```

#### 4. Run Backend
```bash
# Using Maven wrapper
mvnw spring-boot:run

# Or with Maven
mvn spring-boot:run
```

**Expected Output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| ._ \_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.5.7)

EcoBazaarX started in 15.234 seconds
```

---

### Frontend Setup (React + Vite)

#### 1. Navigate to Frontend Directory
```bash
cd frontend
```

#### 2. Install Dependencies
```bash
npm install
```

#### 3. Start Development Server
```bash
npm run dev
```

**Expected Output:**
```
  VITE v5.x.x  ready in xxx ms

  ➜  Local:   http://localhost:3000/
  ➜  press h to show help
```

The application will automatically open in your browser at `http://localhost:3000`.

---

## API Integration Details

### API Base URL
- **Development**: `http://localhost:8080/api`
- **Production**: `https://your-domain.com/api`

### Available Endpoints

#### Authentication (`/api/auth`)
- `POST /login` - User login
- `POST /signup` - User registration
- `POST /send-otp` - Send OTP for phone login
- `POST /login/otp` - Login with OTP
- `GET /me` - Get current user
- `POST /logout` - Logout

#### Products (`/api/products`)
- `GET /` - Get all products with filters
- `GET /{id}` - Get product details
- `POST /` - Create product (seller only)
- `PUT /{id}` - Update product (seller only)
- `DELETE /{id}` - Delete product (seller only)

#### Admin (`/api/admin`)
- `GET /stats` - Dashboard statistics
- `GET /users` - Get all users
- `GET /sellers` - Get all sellers
- `POST /sellers/{id}/approve` - Approve seller

#### Carbon (`/api/carbon`)
- `GET /insights/{userId}` - Get user carbon insights
- `GET /analysis` - Get carbon analysis

#### Recommendations (`/api/recommendations`)
- `GET /personalized/{userId}` - Get personalized recommendations

---

## Configuration Files

### Frontend Configuration Files

#### `vite.config.js`
```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      rewrite: (path) => path,
    },
  },
}
```

#### Service Files (Updated)
All service files in `frontend/src/services/` have been updated to:
1. Use relative API paths (`/api/*` instead of full URLs)
2. Set `MOCK_MODE = false` to use real backend
3. Include proper authentication headers

**Services Updated:**
- `authService.js` - Authentication
- `productService.js` - Product management
- `adminService.js` - Admin operations
- `carbonService.js` - Carbon analytics
- `recommendationService.js` - AI recommendations

---

## Backend Configuration

### CORS Configuration (`CorsConfig.java`)
The backend includes CORS configuration to allow requests from:
- `http://localhost:3000` (Vite dev server)
- `http://127.0.0.1:3000` (Local alternative)
- `http://localhost:5173` (Vite alternative port)
- `http://127.0.0.1:5173` (Vite alternative)

### Security Configuration
- CSRF protection disabled (JWT-based security)
- CORS enabled for frontend origin
- JWT authentication filter active
- Session management: Stateless

---

## Running Both Services

### Terminal Setup

**Terminal 1 - Backend:**
```bash
cd Infosys
mvnw spring-boot:run
# Backend running on http://localhost:8080
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm run dev
# Frontend running on http://localhost:3000
```

---

## Testing the Integration

### 1. Check Backend Health
```bash
curl http://localhost:8080/actuator/health
```

### 2. Test Frontend
Visit `http://localhost:3000` in your browser.

### 3. Check Network Requests
Open Developer Tools (F12) → Network tab to verify API calls are going to `/api/*`.

### 4. Test API Endpoint
```bash
curl -X GET http://localhost:8080/api/products
```

---

## Troubleshooting

### Issue: CORS Error
**Solution:**
1. Ensure backend is running on port 8080
2. Check `CorsConfig.java` has correct frontend origin
3. Clear browser cache and reload

### Issue: 404 on API Calls
**Solution:**
1. Verify backend routes exist in controllers
2. Check proxy configuration in `vite.config.js`
3. Ensure API base URLs in services use relative paths (`/api/*`)

### Issue: JWT Token Issues
**Solution:**
1. Check `JWT_SECRET` in `application.properties`
2. Verify token is stored in localStorage
3. Check `JwtFilter.java` configuration

### Issue: Database Connection Error
**Solution:**
1. Verify PostgreSQL is running
2. Check database exists: `psql -l`
3. Verify credentials in `application.properties`

### Issue: Port Already in Use
```bash
# Find process using port 3000
lsof -i :3000

# Find process using port 8080
lsof -i :8080

# Kill process (replace PID with actual ID)
kill -9 PID
```

---

## Environment Variables

### Backend (`application.properties`)
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/EcoBazarX_db
spring.datasource.username=postgres
spring.datasource.password=vashvi

# JWT
JWT_SECRET=U2VjdXJlS2V5Rm9ySm9obkRvZTEyMzQ1Njc4OQ==
JWT_EXPIRATION=108000000

# AI Services
spring.ai.openai.api-key=sk-proj-XXXX
spring.ai.ollama.base-url=http://localhost:11434
```

### Frontend
No environment variables needed for development.
For production, update `API_BASE` in service files.

---

## Deployment

### Frontend Build
```bash
cd frontend
npm run build

# Output in dist/
# Deploy to static hosting (Vercel, Netlify, etc.)
```

### Backend Build
```bash
cd Infosys
mvn clean package

# Run jar file
java -jar target/EcoBazarX-0.0.1-SNAPSHOT.jar
```

---

## Development Workflow

### Making Backend Changes
1. Make changes in `Infosys/src/main`
2. Backend auto-reloads (devtools enabled)
3. Test with frontend

### Making Frontend Changes
1. Make changes in `frontend/src`
2. Vite hot-reloads
3. Changes visible immediately

### Creating New Endpoints
1. Create Controller in backend
2. Update service in frontend with new API call
3. Test through UI

---

## Support & References

### Documentation Links
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [React Docs](https://react.dev)
- [Vite Docs](https://vitejs.dev)
- [JWT Authentication](https://jwt.io)

### Useful Commands

```bash
# Clean everything
mvn clean
npm cache clean --force

# Run tests
mvn test
npm test

# Check ports
netstat -ano | findstr :3000  # Windows
lsof -i :3000                 # Mac/Linux

# Build optimized frontend
npm run build

# Preview production build
npm run preview
```

---

## Next Steps

1. **Complete Backend Development**
   - Implement all API endpoints
   - Set up database migrations
   - Add proper error handling

2. **Complete Frontend Development**
   - Remove mock data
   - Implement all UI components
   - Add form validation

3. **Testing**
   - Unit tests for backend
   - Integration tests
   - E2E tests for frontend

4. **Deployment**
   - Set up CI/CD pipeline
   - Configure production database
   - Deploy to cloud (AWS, Azure, etc.)

---

**Last Updated**: November 11, 2025
**Status**: Integration Complete ✓
