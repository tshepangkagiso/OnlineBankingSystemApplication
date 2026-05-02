# 🏦 iNOVA Bank - Cloud-Native Digital Banking Platform

## 📌 Overview

iNOVA Bank is a cloud-native, full-stack digital banking platform built with Spring Boot, Angular, and PostgreSQL. The system simulates core banking operations with enterprise-grade security and financial precision. It supports two account types (Savings & Cheque/Check) with distinct features including daily interest calculation, overdraft management, and comprehensive transaction auditing.

The platform is fully deployed on Microsoft Azure using Docker containers, Azure Container Apps, and Azure Web Apps – making it a production-ready, scalable cloud application.

## 📸 Screenshots

### Authentication Flow
| Login | Register |
|-------|----------|
| ![Login](https://github.com/tshepangkagiso/OnlineBankingSystemApplication/blob/master/screenshots/login.png?raw=true) | ![Register](https://github.com/tshepangkagiso/OnlineBankingSystemApplication/blob/master/screenshots/register.png?raw=true) |

### Main Features
| Dashboard | Transaction History |
|-----------|---------------------|
| ![Dashboard](https://github.com/tshepangkagiso/OnlineBankingSystemApplication/blob/master/screenshots/accounts.png?raw=true) | ![History](https://github.com/tshepangkagiso/OnlineBankingSystemApplication/blob/master/screenshots/logs.png?raw=true) |

### Home Page
![Home Top](https://github.com/tshepangkagiso/OnlineBankingSystemApplication/blob/master/screenshots/home%20top.png?raw=true)
![Home Footer](https://github.com/tshepangkagiso/OnlineBankingSystemApplication/blob/master/screenshots/home%20footer.png?raw=true)

### Azure Cloud Infrastructure
![azure](https://github.com/tshepangkagiso/OnlineBankingSystemApplication/blob/master/screenshots/azure.png?raw=true)
---

## ✨ Features

### 🔐 Security & Authentication
- JWT-based authentication with Spring Security
- Role-based access control (ROLE_USER, ROLE_ADMIN)
- Secure password hashing with BCrypt
- HTTP-only cookie storage for tokens
- Route guards protecting sensitive endpoints

### 💰 Account Management
| Feature | Savings Account | Check Account |
|---------|----------------|---------------|
| Daily Interest | ✅ 8.5% p.a. | ❌ |
| Overdraft Facility | ❌ | ✅ Up to R50,000 |
| Instant Transfers | ✅ | ✅ |
| Monthly Fees | ✅ None | ✅ None |

### 📊 Core Banking Operations
- **Deposits & Withdrawals** - Real-time balance updates
- **Account Opening** - Instant account creation
- **Overdraft Management** - Set limits & toggle protection
- **Transaction History** - Advanced filtering by date, type, amount
- **Interest Calculation** - Automated daily interest on savings

### 🔧 Technical Highlights

#### Financial Precision
- `BigDecimal` arithmetic eliminating floating-point errors
- `HALF_EVEN` rounding mode for banking compliance
- Precise interest calculation with decimal accuracy

#### Data Integrity & Concurrency
- `PESSIMISTIC_WRITE` locking preventing race conditions
- `REQUIRES_NEW` transaction propagation for batch isolation
- Optimized bulk processing for thousands of accounts

#### Auditability
- Immutable `TransactionLog` audit trail
- Pre/post transaction state capture
- Complete transaction history with account type filtering

#### Testing & Quality
- Comprehensive Integration Testing
- MockMvc for controller layer validation
- JUnit for unit testing business logic

---

## ☁️ Deployment (Azure & Docker)

This system is deployed using Microsoft Azure with a containerized backend and a hosted frontend. Docker Hub is used for image storage, and all deployments are done manually via Visual Studio Code.

---

### 🌐 Frontend Deployment (Azure Web App)

The frontend is deployed to an Azure Web App as static files.

### Configuration

| Configuration | Value |
|--------------|-------|
| Service | Microsoft Azure Web App |
| Operating System | Windows |
| Region | South Africa North |
| Pricing Tier | Basic (B1) |

### Deployment Process

- Build the project locally (`ng build --production`)
- Deploy only the contents of the `dist/.../browser` folder using the VS Code Azure App Service extension
- The deployed files are served directly by the Web App

---

## 🐳 Backend Deployment (Docker + Azure Container Apps)

The backend is containerized and deployed using Azure Container Apps.

### Configuration

| Configuration | Value |
|--------------|-------|
| Container Image | `tshepangkagisomashigo/onlinebankingsystembackend:latest` |
| Image Registry | Docker Hub (public repository) |
| Service | Microsoft Azure Container Apps |
| OS | Linux |
| CPU | 0.5 cores |
| Memory | 1 GiB |
| Ingress | Enabled (public endpoint) |
| Port | 8080 |
| Scaling | Consumption-based |

### Build & Push Commands

```bash
docker build -t tshepangkagisomashigo/onlinebankingsystembackend:latest .
docker push tshepangkagisomashigo/onlinebankingsystembackend:latest
```

### Runtime Behavior

- The container is pulled from Docker Hub during deployment
- Azure assigns a public URL once ingress is enabled
- The container runs continuously with automatic restart policies

---

## 🗄️ Database (Azure Hosted)

The system uses a managed database hosted on Microsoft Azure.

### Configuration

| Configuration | Value |
|--------------|-------|
| Hosting | Microsoft Azure |
| Access Control | Firewall rules + credentials |
| Connection | Environment variables supplied to container |

---

## 🔗 Integration Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Frontend App  │────▶│ Azure Container │────▶│    Azure DB     │
│ (Azure Web App) │     │ Apps (Docker)   │     │ (PostgreSQL)    │
│                 │◀────│     :8080       │◀────│                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

- Frontend is deployed and assigned a public URL
- Backend container is deployed and exposed via Azure Container Apps
- Backend is configured to allow requests from the frontend URL (CORS)
- Frontend is updated to use the backend's public endpoint


## 🛠️ Tech Stack

### Backend

| Technology | Purpose |
|------------|---------|
| Java 17 | Core language |
| Spring Boot 3.x | Application framework |
| Spring Security | Authentication and authorization |
| JWT | Token-based security |
| Spring Data JPA | Database operations |
| Hibernate | Object-relational mapping |
| PostgreSQL | Production database |
| H2 Database | Development and testing |
| Maven | Build automation |

---

### Frontend

| Technology | Purpose |
|------------|---------|
| Angular 20 | User interface framework |
| TypeScript | Type-safe development |
| RxJS | Reactive programming |
| Font Awesome | Icons |
| CSS3 | Styling (dark theme) |

---

### Testing & Development Tools

| Technology | Purpose |
|------------|---------|
| JUnit 5 | Unit testing |
| MockMvc | Integration testing |
| Postman | API testing |
| Git | Version control |

---

### Cloud & DevOps

| Technology | Purpose |
|------------|---------|
| Microsoft Azure | Cloud provider |
| Azure Container Apps | Backend hosting |
| Azure Web App | Frontend hosting |
| Docker | Containerization |
| Docker Hub | Container image registry |
---

## 🏗️ Architecture

**Frontend (Angular)** – Handles UI, stores JWT token, and calls backend APIs.

**Backend (Spring Boot)** – Manages business logic, authentication, and database operations.

**Security** – JWT filters validate every request; passwords hashed with BCrypt.

**Database (PostgreSQL)** – Stores users, accounts, and immutable transaction logs.

**How it flows:** User logs in → Backend returns JWT token → Frontend stores token → Token sent with every request → Backend validates token → Response returned.

**Concurrency:** Pessimistic locking prevents race conditions. Batch interest uses isolated transactions so one failure doesn't kill the whole batch.

**Cloud Flow**: Code → Docker build → Push to Docker Hub → Azure pulls image → Azure Container Apps runs backend → Frontend connects to backend URL → User accesses via public endpoint
---

## 🔄 API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Create new account |
| POST | `/auth/login` | Authenticate & receive JWT |

### Client Operations (Requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/clients/{email}` | Get client by email |
| PUT | `/clients/update/{email}` | Update client info |
| PUT | `/clients/open/savings` | Open savings account |
| PUT | `/clients/open/check` | Open check account |

### Account Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | `/savingsaccounts/deposit/{email}` | Deposit to savings |
| PUT | `/savingsaccounts/withdraw/{email}` | Withdraw from savings |
| PUT | `/checkaccounts/deposit/{email}` | Deposit to check |
| PUT | `/checkaccounts/withdraw/{email}` | Withdraw from check |
| PUT | `/checkaccounts/overdraft/{email}` | Set overdraft limit |
| PUT | `/checkaccounts/toggle/{email}` | Toggle overdraft |

### Transaction History
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/transactionlogs/history/{email}` | Full transaction history |
| GET | `/transactionlogs/history/{email}/{type}` | Filter by account type |
| GET | `/transactionlogs/recent/{email}` | Recent transactions |
| GET | `/transactionlogs/admin/range` | Date range search |
| GET | `/transactionlogs/admin/type/{type}` | Filter by transaction type |
| GET | `/transactionlogs/admin/high-value` | High value transactions |

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 18+ and npm
- PostgreSQL (or use H2 for development)

### Backend Setup

```bash
# Clone repository
git clone https://github.com/yourusername/inova-bank-backend.git

# Navigate to project
cd inova-bank-backend

# Configure database in application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inova_bank
spring.datasource.username=your_username
spring.datasource.password=your_password

# Run with Maven
./mvnw spring-boot:run

# Or build JAR
./mvnw clean package
java -jar target/inova-bank-0.0.1-SNAPSHOT.jar
```

### Frontend Setup

```bash
# Clone repository
git clone https://github.com/yourusername/inova-bank-frontend.git

# Navigate to project
cd inova-bank-frontend

# Install dependencies
npm install --legacy-peer-deps

# Configure API URL (src/environments/environment.ts)
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'
};

# Run development server
ng serve -o
```

### Default Test Account
```
Email: matt@gmail.com
Password: password1234
```

---

## 🧪 Running Tests

### Backend Tests
```bash
# Unit tests
./mvnw test

# Integration tests
./mvnw verify
```

### Frontend Tests
```bash
# Unit tests
ng test

# E2E tests
ng e2e
```

---

## 📊 Database Schema

```sql
-- Core Tables
clients (account_number, email, password, account_holder)
savings_accounts (id, balance, interest_rate, latest_deposit_date)
check_accounts (id, balance, overdraft_limit)
transaction_logs (id, client_email, account_type, transaction_type, pre_balance, post_balance)
```

---

## 🔒 Security Implementation

### JWT Flow
1. Client submits credentials → `/auth/login`
2. Backend validates & returns JWT token
3. Token stored in `sessionStorage`
4. Subsequent requests include `Authorization: Bearer <token>`
5. `JwtAuthFilter` validates token & sets authentication context

### Password Security
- BCrypt hashing with salt rounds = 10
- Passwords never stored in plaintext
- Minimum 12 character requirement

---

## 🎯 Key Challenges & Solutions

### Challenge 1: Concurrent Access & Race Conditions
**Problem:** Simultaneous deposit/withdrawal operations could create inconsistent balances.

**Solution:** Implemented `PESSIMISTIC_WRITE` locking at the database level, ensuring only one transaction can modify an account at a time.

### Challenge 2: Bulk Interest Calculation Failure
**Problem:** A single failed account would roll back interest for all accounts.

**Solution:** Used `@Transactional(propagation = REQUIRES_NEW)` to isolate individual account processing, preventing cascade failures.

### Challenge 3: Floating-Point Precision
**Problem:** Double/float operations caused rounding errors in financial calculations.

**Solution:** Implemented `BigDecimal` with `HALF_EVEN` rounding mode for bank-level precision.

### Challenge 4: Type Mismatch (Decimal ↔ Number)
**Problem:** Backend Decimal objects vs frontend JavaScript numbers.

**Solution:** Created `formatCurrency()` utility method with proper number conversion and ZAR formatting.

---

## 📈 Future Enhancements

- [ ] Two-Factor Authentication (2FA)
- [ ] Biometric login (WebAuthn)
- [ ] Real-time notifications (WebSockets)
- [ ] Export statements (PDF/CSV)
- [ ] Scheduled bill payments
- [ ] Mobile app (Flutter/React Native)
- [ ] Docker & Kubernetes deployment
- [ ] CI/CD pipeline (GitHub Actions)

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing`)
5. Open Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 🙏 Acknowledgments

- Spring Security documentation
- Angular team
- Font Awesome for icons
- All contributors and testers

---

## ⭐ Show Your Support

If this project helped you, please give it a ⭐ on GitHub!
```

This README includes:
- **Professional structure** with badges and clear sections
- **Your technical achievements** (BigDecimal, locking, transaction propagation)
- **Challenge & solution** section highlighting the hardest parts
- **Complete setup instructions** for both backend and frontend
- **API documentation** as reference
- **Architecture diagram** for visual representation
- **Security implementation details**
- **Future enhancements** showing vision
- **Contact info** for recruiter reach-out
```
