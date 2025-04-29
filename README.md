# 🛍️ E-Commerce App Backend (Spring Boot + MyBatis + MySQL)

This is the backend for the full-featured E-Commerce App built using **Spring Boot**, **MyBatis**, and **MySQL**. It powers the Flutter frontend and provides APIs for both **user** and **admin** roles.

---

## ⚙️ Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Spring Security**
- **MyBatis**
- **MySQL**
- **JWT Authentication**
- **Lombok**

---


## 🔐 Features

### 👤 User APIs

- User registration and login (JWT-based)
- Browse categories and products
- View product details
- Add/update/remove from cart
- Checkout and create order
- Manage address
- View my orders

### 🧑‍💼 Admin APIs

- Add/edit/delete products
- Add/edit/delete categories
- Manage orders (view/change status)
- View all users and their orders

---

## 🛠️ How to Run

### 1. Clone the repository

```bash
git clone https://github.com/your-username/ecommerce-backend.git
cd ecommerce-backend

2. Configure Database
Update your src/main/resources/application.properties:

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/database_name
    username: your_username
    password: your_password
  mybatis:
    mapper-locations: classpath:mapper/*.xml

jwt:
  secret: your_jwt_secret_key

Make sure your MySQL server is running and a database named ecommerce exists.

3. Run the application
./mvnw spring-boot:run


🧠 Notes
Make sure CORS is enabled for frontend Flutter app

Secure endpoints with Spring Security and JWT filter

Separate role-based access for User and Admin

📬 Feedback
If this backend helped your app development, please consider giving a ⭐ on GitHub!

