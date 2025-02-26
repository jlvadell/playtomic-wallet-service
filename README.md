# 🚀 Playtomic Wallet Service (Technical Test)

This project is a **proof of concept** implementation of a **wallet management service** for Playtomic. It enables users to manage their balance, top-up using a credit card, and retrieve wallet information.

## **📌 Features Implemented**
✅ **Get Wallet by ID** - Retrieve the wallet balance.  
✅ **Top-Up Wallet** - Charge a credit card and update wallet balance.  
✅ **Concurrency Handling** - Atomic balance updates with MongoDB transactions.  
✅ **Third-Party Payment Integration** - Uses a Stripe simulator for payments.  
✅ **OpenAPI Documentation** - API schema is auto-generated from `wallet-service-api.yml`.  
✅ **Security** - JWT authentication (mocked for this proof of concept).  
✅ **Testing**
- **Unit tests** for business logic.
- **Integration tests** with embedded MongoDB (Flapdoodle).
- **Acceptance tests** using **Karate UI** with a WireMock server.

---

## **🛠️ Architecture**
This project follows a **MVC Architecture**:

### **📂 Folder Structure**
```
📦 exercise-wallet 
┣ 📂 docker/ # Docker files
┣ 📂 postman/ # Postman collections
┣ 📜 README.md # This file
┗ 📂 src/**/java # source code
    ┣ 📂 config/ # configuration files (jwt, websecurity, etc)
    ┣ 📂 controller/ # REST Controllers, entry points
    ┣ 📂 exception/ # Application exceptions
    ┣ 📂 model/ # Application models
    ┣ 📂 repository/ # DB repositories
    ┗ 📂 service/ # internal and External services (Wallet, Stripe) 
```

## **🔗 API Endpoints**
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/v1/wallets/{wallet-id}` | Retrieve wallet details |
| `POST` | `/v1/wallets/{wallet-id}/transactions` | Top-up balance using a card |

Check `📂 postman/` for a Postman collection with the API endpoints.

📌 **Authentication:**
- Uses a **mock JWT filter** for authorization.
- To authenticate, include a `Authorization` header with value `Bearer {RANDOM_STRING}`

📌 **API Documentation:**
- **OpenApi Spec** is located at `📂 /src/main/resources/api.yaml`.

---

## **🛠️ Technologies Used**
✅ **Java 17**  
✅ **Spring Boot 3**  
✅ **MongoDB**
✅ **MapStruct** (for Mappings)  
✅ **WireMock** (mocking third-party APIs)  
✅ **Karate UI** (Acceptance tests)  
✅ **JaCoCo + SonarQube** (Code coverage & quality analysis)  
✅ **Docker** (for MongoDB & WireMock)

---

## **🧪 Testing Strategy**
- ✅ **Unit Tests** → Covers **Main functionality**.
- ✅ **Integration Tests** → Covers **MongoDB interactions**.
- ✅ **Acceptance Tests** → Uses **Karate UI** to test features.
- ✅ **Quality gates** → **JaCoCo reports** analyzed in **SonarQube**.

---

## **🚀 Running the Application**

### 🧪 TESTS
Run **All tests**:
```bash
mvn clean verify
```

Note: No docker needed, it uses embedded MongoDB and WireMock.

### **🚀 Run the Application**

#### 📦 Start Docker Compose (MongoDB & WireMock) for local development
```bash
docker-compose -f docker/docker-compose.yml up -d
```
#### 🚀 Run the Spring Boot Application
```bash
mvn spring-boot:run
```

---

## **✨ Further Improvements**

### **🪲 Fixes**
- **Sonar Integration** -> Sonar isn't picking up jacoco reports.
- **Coverage** -> Increase test coverage.
- **Fix TODOs** -> Fix all the TODOs in the code.

### **🚀 Enhancements**
- **Refund Feature** -> Implement refund endpoint.
- **Transaction History** -> Implement transaction history endpoint.
- **Use Feign for Stripe** -> Use Feign for a more robust Stripe integration.

### **🔧 Technical Debt**
- **Error Handling** -> Better error handling.
- **Logging** -> Better logging.
- **Metrics** -> Grafana integration.

---

## **💭 Final Thoughts**

This is a solution that tries to use a simple architecture to solve the problem. 
It's not perfect, but it's a good starting point. I've tried to cover the main functionality with tests,
but there's always room for improvement.





