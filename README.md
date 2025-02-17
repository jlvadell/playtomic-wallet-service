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
  ✅ **CI/CD Ready** - Includes JaCoCo coverage reports with SonarQube.

---

## **🛠️ Architecture**
This project follows a **Hexagonal Architecture** using a **modular structure**:

### **📂 Folder Structure**
```
📦 exercise-wallet 
┣ 📂 docker/ # Docker files
┣ 📂 postman/ # Postman collections
┣ 📂 boot/ # Main entry point (Spring Boot) 
┣ 📂 contract/ # REST Controllers + OpenAPI Specification 
┣ 📂 application/ # Command/Query Handlers (CQRS) 
┣ 📂 domain/ # Core Business Logic (Entities, Interfaces) 
┣ 📂 infrastructure/ # Persistence, External APIs (MongoDB, Stripe) 
┗ 📜 README.md # This file
```

### **📌 Module Responsibilities**
| **Module**         | **Responsibilities** |
|--------------------|----------------------|
| **boot**          | Starts the Spring Boot app. Loads dependencies. |
| **contract**      | Exposes REST API using OpenAPI-generated controllers. |
| **application**   | Implements **CQRS** (Commands & Queries). Handles validation & business flows. |
| **domain**        | Defines **Entities, Value Objects, Interfaces** (pure business logic). |
| **infrastructure**| Implements repositories, integrates MongoDB & Stripe API. |

---

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
- **OpenApi Spec** is located at `📂 contract/src/main/resources/api.yaml`.

- **OpenAPI Spec**: `contract/src/main/resources/rest-api/wallet-service-api.yml`

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

### 📦 Docker Compose (MongoDB & WireMock)
```bash
docker-compose -f docker/docker-compose.yml up -d
```

### **🚀 Run the Application**
```bash
mvn spring-boot:run
```

---

## **✨ Further Improvements**

### **🪲 Fixes**
- **Sonar Integration** -> Sonar isn't picking up jacoco reports.
- **Coverage** -> Increase test coverage.
- **Find Wallet transaction** -> Implement a way to find a wallet transaction by ID.

### **🚀 Enhancements**
- **Refund Feature** -> Implement refund endpoint.
- **Transaction History** -> Implement transaction history endpoint.
- **Use Feign for Stripe** -> Use Feign for a more robust Stripe integration.
- **Project Loom** -> Upgrade to Java 21 and use Project Loom for better concurrency.

### **🔧 Technical Debt**
- **Error Handling** -> Better error handling.
- **Logging** -> Better logging.
- **Metrics** -> Grafana integration.
- **Use of testcontainers** -> Use testcontainers for tests.

---

## **💭 Final Thoughts**

This project presents one on many solutions, to this exercise. 
I have tried to keep it simple, yet complete, focusing on the main requirements. 
I have also tried to follow best practices, and keep the code clean and organized.
There's probably a better solution out there but this is the one I came up with in the time I had.





