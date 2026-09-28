# 📊 Portfolio Management System with AI

A full-stack **Portfolio Management System** developed using **Java, Spring Boot, PostgreSQL, HTML, CSS, JavaScript, and AI-powered functionality**.

The application provides users with a centralized platform to manage their investment portfolio, monitor stocks, perform buy/sell transactions, manage wallet balances, maintain watchlists, and receive email notifications for important investment activities.

---

## 🚀 Project Overview

The **Portfolio Management System with AI** is a web-based application designed to simplify stock portfolio and investment management.

Users can register, verify their account through OTP, securely log in, manage their portfolio, monitor stocks, buy and sell stocks, manage wallet transactions, maintain a personalized watchlist, and interact with an AI-powered chatbot.

The system also provides **automated email notifications** for important activities such as:

- Account OTP verification
- Stock purchase
- Stock sale
- Stock profit alerts
- Stock loss alerts

The backend is developed using **Spring Boot** with a layered architecture consisting of controllers, services, repositories, DTOs, entities, and security configuration.

---

# ✨ Key Features

## 🔐 Authentication & Security

- User registration
- OTP-based email verification
- User login
- JWT-based authentication
- Secure password handling
- Protected APIs
- Authentication-based user access
- Role-based functionality

---

## 📧 Email Notification System

The application provides email notifications for important user and investment events.

### OTP Verification Email

During registration, users receive an OTP through email for account verification.

![OTP Verification Email](screenshots/OTP_verification_Mail.jpeg)

### 📈 Stock Profit Alert

When a user's stock position is in profit, the system can notify the user through email.

![Stock Profit Alert](screenshots/Stock_Alert.jpeg)

### 📉 Stock Loss Alert

When a user's stock position is in loss, the system can notify the user through email.

![Stock Loss Alert](screenshots/Loss_Alert.jpeg)

### 💹 Stock Purchase Notification

When a user purchases a stock, the system sends an email notification related to the purchase.

![Stock Purchase Notification](screenshots/Stock_Alert.jpeg)

### 📤 Stock Sale Notification

When a user sells a stock, the system sends an email notification confirming the sale.

![Stock Sold Notification](screenshots/Stock_Sold_Alert.jpeg)

Email functionality uses SMTP configuration through Spring Mail.

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Sensitive email credentials are stored through environment variables.

---

# 📈 Portfolio Management

Users can manage their investment portfolio through the application.

Features include:

- Portfolio management
- View portfolio holdings
- Track invested stocks
- Monitor stock positions
- Track portfolio-related transactions
- View investment information

![Portfolio](screenshots/portfolio.png)

---

# 📊 Stock Management

The application provides stock-related functionality for managing and monitoring available stocks.

Features include:

- Stock information
- Stock price/data management
- Stock history
- Stock monitoring
- Stock selection for transactions
- Portfolio stock tracking

---

# 💹 Buy & Sell Stocks

Users can perform stock transactions through the application.

## Buy Stock

The user can select a stock, enter the required quantity, and perform a purchase transaction.

The system updates the relevant portfolio and transaction records and sends the user an email notification.

## Sell Stock

Users can sell stocks held in their portfolio.

The system processes the sale, updates the relevant records, and sends the user a stock-sale email notification.

![Tradebook](screenshots/tradebook.png)

---

# 👛 Wallet Management

The system provides wallet functionality for managing investment-related transactions.

Features include:

- Wallet balance
- Wallet transactions
- Credit/debit activity
- Transaction history
- Investment-related wallet operations

![Wallet](screenshots/wallet.png)

---

# ⭐ Watchlist

Users can maintain a personalized list of stocks they want to monitor.

Features include:

- Add stock to watchlist
- Remove stock from watchlist
- View selected stocks
- Maintain personalized watchlist
- Quickly access monitored stocks

![Watchlist](screenshots/Watchlist%20%282%29.png)

![Add to Watchlist](screenshots/addToWatchlist.png)

![Added to Watchlist](screenshots/addedToWatchlist.png)

---

# 🤖 AI Chatbot

The application includes an **AI-powered chatbot** that provides an interactive conversational interface.

The chatbot component is maintained in:

```text
chatbot_backend/
```

Users can interact with the chatbot and receive AI-generated responses.

![AI Chatbot](screenshots/chatbot.png)

![AI Chatbot Reply](screenshots/chatbotReply.png)

---

# 👨‍💼 Admin Panel

The application includes administrative functionality for managing application-level information and user-related data.

Admin functionality demonstrated in the application includes:

- Admin dashboard
- Recent messages
- User portfolio information
- User watchlists
- Tradebook information
- Administrative messaging
- Application data monitoring

### Admin Dashboard

![Admin Panel](screenshots/adminPanel.png)

### Admin Messages

![Admin Messages](screenshots/adminMesg.png)

![Admin Recent Messages](screenshots/Admin_recent_messages.png)

### Admin Tradebook

![Admin Tradebook](screenshots/adminTradebook.png)

### Admin User Portfolio

![Admin User Portfolio](screenshots/adminUserPortfolio.png)

### Admin Watchlist

![Admin Watchlist](screenshots/adminWatchlist.png)

---

# 🖥️ Application Screenshots

## 🔑 Login

![Login Page](screenshots/loginPage.png)

## 📝 Registration

![Registration Page](screenshots/registrationPage.png)

## 🔢 OTP Verification

![OTP Verification](screenshots/otpVerify.png)

## 📊 Dashboard

![Dashboard](screenshots/dashboard.png)

## 📈 Portfolio

![Portfolio](screenshots/portfolio.png)

## 📒 Tradebook

![Tradebook](screenshots/tradebook.png)

## 💰 Wallet

![Wallet](screenshots/wallet.png)

## ⭐ Watchlist

![Watchlist](screenshots/Watchlist%20%282%29.png)

## 🤖 AI Chatbot

![AI Chatbot](screenshots/chatbot.png)

---

# 🏗️ System Architecture

The application follows a layered Spring Boot architecture.

```text
                         USER
                           │
                           ▼
                ┌────────────────────┐
                │     Frontend       │
                │ HTML / CSS / JS    │
                │ Templates          │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │    Controllers     │
                │   Web / REST API   │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │      Services      │
                │   Business Logic   │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │    Repositories    │
                │   Spring Data JPA  │
                └─────────┬──────────┘
                          │
                          ▼
                ┌────────────────────┐
                │    PostgreSQL      │
                │     Database       │
                └────────────────────┘

                          │
                          ├──────────────► Email / SMTP
                          │
                          └──────────────► AI Chatbot
```

---

# 🛠️ Technology Stack

## Backend

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- Spring Security
- JWT
- REST APIs
- Maven

## Frontend

- HTML5
- CSS3
- JavaScript
- Templates / Thymeleaf

## Database

- PostgreSQL
- SQL
- JPA/Hibernate

## AI

- Python
- AI-powered chatbot
- Separate chatbot backend

## Email

- Spring Mail
- SMTP
- Gmail SMTP

## Development Tools

- IntelliJ IDEA
- Spring Tool Suite
- Postman
- Git
- GitHub

## Deployment

- Docker
- Dockerfile
- Environment variables
- PostgreSQL cloud database

---

# 🗄️ Database

The application uses PostgreSQL for persistent data storage.

Major database tables include:

```text
users
portfolio
stocks
stock_history
transaction
wallet
wallet_transaction
watchlist
admin_message
```

These tables support the relationships between users, portfolios, stocks, transactions, wallets, watchlists, and administrative information.

---

# 🔒 Environment Configuration

Sensitive credentials are not committed to GitHub.

The application uses environment variables for production configuration.

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}

jwt.secret=${JWT_SECRET}

server.port=${PORT:8080}
```

Required environment variables:

```text
DATABASE_URL
DB_USERNAME
DB_PASSWORD
MAIL_USERNAME
MAIL_PASSWORD
JWT_SECRET
PORT
```

The actual passwords, database credentials, email credentials, and JWT secret should never be committed to the repository.

---

# 🐳 Docker Support

The project includes a Dockerfile for containerized deployment.

The Docker configuration uses:

- Maven
- Java 21
- Eclipse Temurin
- Spring Boot

The application can be built using:

```bash
mvn clean package -DskipTests
```

Docker build:

```bash
docker build -t portfolio-management-system .
```

Docker run:

```bash
docker run -p 8080:8080 portfolio-management-system
```

---

# ▶️ Running Locally

## Prerequisites

Install:

- Java 21
- Maven
- PostgreSQL
- Git

## Clone the Repository

Clone the project from GitHub and open it in IntelliJ IDEA or another Java IDE.

## Configure Environment Variables

Configure:

```text
DATABASE_URL
DB_USERNAME
DB_PASSWORD
MAIL_USERNAME
MAIL_PASSWORD
JWT_SECRET
```

## Build

```bash
mvn clean package
```

## Run

Run the Spring Boot application from IntelliJ IDEA or execute the generated JAR.

Default application port:

```text
8080
```

---

# 🔄 Main Application Flow

```text
Registration
     │
     ▼
OTP Verification
     │
     ▼
Login
     │
     ▼
JWT Authentication
     │
     ▼
Dashboard
     │
     ├────────► Portfolio
     │
     ├────────► Stocks
     │
     ├────────► Buy Stock
     │              │
     │              ▼
     │        Purchase Email
     │
     ├────────► Sell Stock
     │              │
     │              ▼
     │          Sale Email
     │
     ├────────► Wallet
     │
     ├────────► Watchlist
     │
     └────────► AI Chatbot

Stock Monitoring
     │
     ├────────► Profit Condition
     │              │
     │              ▼
     │        Profit Email Alert
     │
     └────────► Loss Condition
                    │
                    ▼
              Loss Email Alert
```

---

# 🎯 Project Objectives

The major objectives of the project are:

- Develop a complete portfolio management web application
- Implement secure authentication
- Implement OTP verification
- Manage stock portfolios
- Manage stock information
- Implement buy/sell transactions
- Maintain transaction history
- Implement wallet management
- Implement watchlist functionality
- Implement email notifications
- Implement profit/loss alerts
- Integrate an AI chatbot
- Develop administrative functionality
- Use PostgreSQL for persistent storage
- Apply Spring Boot layered architecture
- Prepare the application for Docker deployment

---

# 💡 Key Learning & Technical Experience

This project demonstrates practical experience in:

- Java development
- Spring Boot
- REST API development
- Spring Security
- JWT authentication
- OTP verification
- Spring Data JPA
- Hibernate
- PostgreSQL
- Database relationships
- Business logic implementation
- Email/SMTP integration
- AI integration
- Python backend integration
- Frontend/backend integration
- Maven
- Docker
- Git
- GitHub
- Environment-based configuration

---

# 🔮 Future Enhancements

Potential future improvements include:

- Real-time stock market data
- Advanced portfolio analytics
- Interactive profit/loss charts
- Advanced stock performance graphs
- Mobile application
- Push notifications
- Advanced AI portfolio analysis
- Automated CI/CD deployment
- Advanced financial reports
- Cloud-based production deployment

---

# 📌 Project Status

The source code is maintained on GitHub.

The project includes:

- Spring Boot backend
- PostgreSQL database integration
- JWT authentication
- OTP verification
- Portfolio management
- Stock management
- Buy/sell transactions
- Wallet management
- Watchlist
- Email notifications
- Profit/loss alerts
- AI chatbot
- Admin panel
- Docker deployment configuration

---

# 👩‍💻 Developer

## Nikkita Kumari

Portfolio Management System with AI

Developed as part of software internship project work at **Bonrix Software Systems, Ahmedabad, Gujarat, India**.

---

# 📄 Disclaimer

This project is developed for educational, internship, portfolio, and demonstration purposes.

Stock-market information and AI-generated information shown by the application should not be considered financial advice.