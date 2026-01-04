# 🥬 Fresh Fridge - Backend API

> Spring Boot REST API for Fresh Fridge application

## 📋 Overview

This is the backend API service for Fresh Fridge, a smart shopping list and kitchen management application. Built with Spring Boot 3.5.9 and Java 21, it provides RESTful APIs for managing users, groups, shopping lists, kitchen inventory, recipes, and meal planning.

## 🛠️ Tech Stack

### Core
- **Java**: 21
- **Spring Boot**: 3.5.9
- **Maven**: Build tool

### Frameworks & Libraries
- **Spring Data JPA**: Database ORM
- **Spring Security**: Authentication & authorization
- **Spring Boot Web**: REST API
- **Spring Boot Validation**: Request validation
- **Spring Boot Mail**: Email service

### Database
- **PostgreSQL**: Primary database
- **Flyway**: Database migration tool

### Security & Authentication
- **JWT (jjwt)**: 0.12.6 - Token-based authentication
- **BCrypt**: Password hashing

### Object Mapping
- **MapStruct**: 1.5.5.Final - DTO mapping

### Cloud Services
- **Firebase Admin SDK**: 9.2.0 - FCM push notifications
- **Cloudinary**: 1.36.0 - Image storage

### Development Tools
- **Lombok**: Code generation
- **Spring Boot DevTools**: Hot reload

---

## 🚀 Quick Start

### Prerequisites

- **Java**: JDK 21
- **Maven**: 3.6 or higher
- **PostgreSQL**: 12 or higher
- **Git**: For version control

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/QuanTuanHuy/FreshFridge.git
   cd FreshFridge/backend
   ```

2. **Configure environment variables**

   Create a `.env` file in the backend directory:

   ```env
   # Database Configuration
   DB_USERNAME=your_db_username
   DB_PASSWORD=your_db_password
   DB_URL=jdbc:postgresql://localhost:5432/freshfridge

   # JWT Configuration
   JWT_SECRET=your_jwt_secret_key
   JWT_EXPIRATION=86400000

   # Mail Configuration
   MAIL_HOST=smtp.gmail.com
   MAIL_PORT=587
   MAIL_USERNAME=your_email@gmail.com
   MAIL_PASSWORD=your_email_app_password
   MAIL_FROM=noreply@freshfridge.com
   ```

3. **Create PostgreSQL database**

   ```sql
   CREATE DATABASE freshfridge;
   ```

4. **Configure Firebase (Optional - for Push Notifications)**

   Place your Firebase service account key in:
   ```
   src/main/resources/fcm/config.json
   ```

5. **Run the application**

   ```bash
   # Using Maven wrapper (recommended)
   ./mvnw spring-boot:run

   # Or using installed Maven
   mvn spring-boot:run

   # Or using the shell script
   ./run-dev.sh
   ```

6. **API will be available at**
   
   ```
   http://localhost:8080/api/v1
   ```

---

## 📦 Available Scripts

```bash
# Run application in development mode
./mvnw spring-boot:run

# Build the application
./mvnw clean install

# Run tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=FreshfridgeApplicationTests

# Run tests with coverage
./mvnw test jacoco:report

# Package as JAR
./mvnw clean package

# Clean build artifacts
./mvnw clean
```

---

## 📁 Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── hust/
│   │   │       └── project/
│   │   │           └── freshfridge/
│   │   │               ├── FreshfridgeApplication.java
│   │   │               ├── application/         # Application layer
│   │   │               │   ├── dto/            # Data Transfer Objects
│   │   │               │   ├── scheduler/      # Background tasks & cron jobs
│   │   │               │   └── usecase/        # Business use cases
│   │   │               ├── domain/             # Domain layer
│   │   │               │   ├── constant/       # Domain constants
│   │   │               │   └── entity/         # Domain entities
│   │   │               ├── infrastructure/     # Infrastructure layer
│   │   │               │   ├── repository/     # Database repositories
│   │   │               │   └── service/        # External services
│   │   │               └── presentation/       # Presentation layer
│   │   │                   └── controller/     # REST API Controllers
│   │   └── resources/
│   │       ├── application.yaml               # Spring Boot configuration
│   │       ├── db/
│   │       │   └── migration/                 # Flyway database migrations
│   │       │       ├── V1__create_users_table.sql
│   │       │       ├── V2__create_groups_table.sql
│   │       │       ├── V3__create_common_tables.sql
│   │       │       ├── V4__create_foods_table.sql
│   │       │       ├── V5__create_kitchen_items_table.sql
│   │       │       ├── V6__create_shopping_tables.sql
│   │       │       ├── V7__Create_recipe_tables.sql
│   │       │       ├── V8__Create_meal_plan_table.sql
│   │       │       └── V9__Add_fcm_token_to_users.sql
│   │       ├── fcm/
│   │       │   └── config.json               # Firebase Cloud Messaging config
│   │       └── META-INF/
│   └── test/
│       └── java/
│           └── hust/project/freshfridge/
├── pom.xml                                    # Maven dependencies
├── Dockerfile                                 # Docker configuration
└── run-dev.sh                                # Development run script
```

### Architecture

This project follows **Clean Architecture** principles with clear separation of concerns:

- **Presentation Layer**: REST Controllers handling HTTP requests/responses
- **Application Layer**: Use cases implementing business logic, DTOs for data transfer
- **Domain Layer**: Core business entities and domain logic
- **Infrastructure Layer**: Database repositories, external service integrations

---

## 🔧 Configuration

### Application Configuration

Key settings in `application.yaml`:

```yaml
spring:
  application:
    name: freshfridge
  datasource:
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: none
  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration
  servlet:
    multipart:
      max-file-size: 5MB
      max-request-size: 10MB

server:
  port: 8080
  servlet:
    context-path: /api/v1
```

### Database Migrations

The project uses Flyway for database version control. Migrations are located in:
```
src/main/resources/db/migration/
```

Migration naming convention: `V{version}__{description}.sql`

---

## 📱 Building for Production

### Build JAR

```bash
# Build with Maven
./mvnw clean package

# JAR file will be in target/freshfridge-0.0.1-SNAPSHOT.jar
```

### Run JAR

```bash
java -jar target/freshfridge-0.0.1-SNAPSHOT.jar
```

### Docker Build

```bash
# Build Docker image
docker build -t freshfridge-backend .

# Run container
docker run -p 8080:8080 \
  -e DB_USERNAME=your_db_user \
  -e DB_PASSWORD=your_db_pass \
  -e DB_URL=jdbc:postgresql://host:5432/freshfridge \
  -e JWT_SECRET=your_secret \
  freshfridge-backend
```

---

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=FreshfridgeApplicationTests

# Run tests with coverage
./mvnw test jacoco:report
```

---

## 🐛 Troubleshooting

### Database connection failed

- Check PostgreSQL is running: `pg_isready`
- Verify database exists: `psql -l`
- Check credentials in `.env` file

### Port 8080 already in use

```bash
# Find process using port 8080 (Windows)
netstat -ano | findstr :8080

# Kill the process
taskkill /PID <process_id> /F

# Or change port in application.yaml
```

### Flyway migration failed

```bash
# Reset Flyway migrations (development only!)
./mvnw flyway:clean
./mvnw flyway:migrate

# Or manually drop and recreate database
```

### Build failed - "package does not exist"

```bash
# Clean and rebuild
./mvnw clean install -U
```

---

## 📚 API Documentation

See [API Documentation](../docs/api_document.md) for detailed API endpoints and usage.

---

## 📚 Additional Documentation

### Backend Implementation Details
- [Backend Overview](../docs/backend-implementation/00-overview.md)
- [User Domain](../docs/backend-implementation/01-user-domain.md)
- [Group Domain](../docs/backend-implementation/02-group-domain.md)
- [Food Domain](../docs/backend-implementation/03-food-domain.md)
- [Kitchen Domain](../docs/backend-implementation/04-kitchen-domain.md)
- [Shopping Domain](../docs/backend-implementation/05-shopping-domain.md)
- [Meal Domain](../docs/backend-implementation/06-meal-domain.md)
- [Recipe Domain](../docs/backend-implementation/07-recipe-domain.md)
- [Common Domain](../docs/backend-implementation/08-common-domain.md)
- [Suggestion & Order](../docs/backend-implementation/09-suggestion-and-order.md)

---

## 📄 License

This project is licensed under the MIT License.

---

## 👥 Support

If you have any questions or issues, please open an issue on GitHub or contact the development team.

---

**Happy Coding! 🚀**
