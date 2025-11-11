# Quick Start Guide - EcoBazaarX Integration

## 🎯 30-Second Setup

### Prerequisites Check
- ✅ JDK 21 installed
- ✅ Maven installed
- ✅ Node.js v16+ installed
- ✅ PostgreSQL running with `EcoBazarX_db` created

### Start Backend
```powershell
# Windows PowerShell
cd Infosys
.\mvnw.cmd spring-boot:run

# Expected: Application started in ~15 seconds on port 8080
```

### Start Frontend
```powershell
# Windows PowerShell (New Terminal Tab)
cd frontend
npm run dev

# Expected: Application opens at http://localhost:3000
```

---

## ✅ Verify Integration

### Backend Health Check
```powershell
curl http://localhost:8080/actuator/health
```
Expected response: `{"status":"UP"}`

### Frontend Check
- Browser opens to `http://localhost:3000`
- No console errors (F12 to check)

### API Communication Check
1. Open DevTools (F12) → Network tab
2. Try any action that makes an API call
3. Look for request URLs like `http://localhost:8080/api/...`
4. Should see response with data or error message

---

## 🔑 Test Credentials

### Admin Login
- **Email**: `admin@ecobazaarx.com`
- **Password**: `EcoAdmin@2024`

### Customer Signup
- Create account with any email
- Instant access to customer dashboard

### Seller Signup
- Create account with any email, role as "Seller"
- Account will be PENDING until admin approves

---

## 📁 Project Structure

```
InfosysSpringboard/
├── Infosys/              ← Backend (Spring Boot)
│   ├── src/main/java/    ← Controllers, Services, Models
│   ├── pom.xml          ← Dependencies
│   └── mvnw.cmd         ← Maven wrapper (Windows)
│
└── frontend/            ← Frontend (React + Vite)
    ├── src/
    │   ├── services/    ← API Service Classes
    │   ├── components/  ← React Components
    │   └── pages/       ← Page Components
    ├── vite.config.js   ← Proxy Configuration
    └── package.json     ← Dependencies
```

---

## 🚨 Common Issues

| Issue | Solution |
|-------|----------|
| Port 8080 in use | Kill process: `taskkill /PID <PID> /F` |
| Port 3000 in use | Change in vite.config.js or kill process |
| API 404 errors | Verify backend controller endpoints exist |
| CORS errors | Ensure CorsConfig.java is deployed |
| Database errors | Create database: `createdb EcoBazarX_db` |
| Token not sent | Check localStorage has `ecobazaarx_token` |

---

## 📋 API Endpoints Available

### Authentication
```
POST   /api/auth/login           - User login
POST   /api/auth/signup          - User registration
POST   /api/auth/send-otp        - Send OTP
POST   /api/auth/login/otp       - OTP login
GET    /api/auth/me              - Get current user
```

### Products
```
GET    /api/products             - Get all products
GET    /api/products/{id}        - Get product details
POST   /api/products             - Create product (seller)
PUT    /api/products/{id}        - Update product (seller)
DELETE /api/products/{id}        - Delete product (seller)
```

### Admin
```
GET    /api/admin/stats          - Dashboard stats
GET    /api/admin/users          - Get all users
POST   /api/admin/sellers/{id}/approve - Approve seller
```

---

## 🛠️ Development Workflow

### Making Changes

**Backend Changes:**
1. Edit files in `Infosys/src/main/`
2. Backend auto-reloads (devtools enabled)
3. Test through frontend

**Frontend Changes:**
1. Edit files in `frontend/src/`
2. Vite hot-reloads automatically
3. Changes visible instantly in browser

### Testing API Calls
```powershell
# Test endpoint from command line
curl -X GET http://localhost:8080/api/products

# Test with header
curl -X GET http://localhost:8080/api/products `
  -H "Authorization: Bearer YOUR_TOKEN"

# Test POST
curl -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{"email":"test@example.com","password":"password"}'
```

---

## 📦 Build for Production

### Frontend Build
```powershell
cd frontend
npm run build      # Creates dist/ folder
npm run preview    # Preview production build
```

### Backend Build
```powershell
cd Infosys
mvn clean package  # Creates target/EcoBazarX-0.0.1-SNAPSHOT.jar
java -jar target/EcoBazarX-0.0.1-SNAPSHOT.jar
```

---

## 🔐 Configuration Files

### Backend (`Infosys/src/main/resources/application.properties`)
Update database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/EcoBazarX_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
server.port=8080
```

### Frontend (`frontend/vite.config.js`)
Proxy configuration (pre-configured):
```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    }
  }
}
```

---

## 🎓 Learning Resources

- **Spring Boot**: https://spring.io/projects/spring-boot
- **React**: https://react.dev
- **Vite**: https://vitejs.dev
- **JWT**: https://jwt.io
- **PostgreSQL**: https://www.postgresql.org/docs/

---

## 📞 Quick Troubleshooting

### Check Services Running
```powershell
# Check backend
curl http://localhost:8080/actuator/health

# Check frontend (open in browser)
# http://localhost:3000

# Find process using port
netstat -ano | findstr :8080
netstat -ano | findstr :3000
```

### View Logs

**Backend**: Check terminal where `mvnw spring-boot:run` is running
**Frontend**: Check browser DevTools (F12) Console tab

### Reset Everything
```powershell
# Stop both services (Ctrl+C in each terminal)
# Clear npm cache
npm cache clean --force

# Clear Maven cache
mvn clean

# Restart services
```

---

## ✨ You're All Set!

Your frontend and backend are now connected and ready for development.

**Start developing with:**
- Backend: `Infosys/` folder on port 8080
- Frontend: `frontend/` folder on port 3000
- Database: PostgreSQL `EcoBazarX_db`

Happy coding! 🎉

---

**Last Updated**: November 11, 2025
