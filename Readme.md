# 🛒 PayCart – Payment Integration Gateway

A full-stack e-commerce payment platform built using **Spring Boot Microservices, React, MySQL, JWT Authentication, Stripe Payments, Eureka Service Discovery, and REST APIs**.

PayCart demonstrates a complete online shopping and payment workflow — from user authentication and product browsing to cart management, secure Stripe payment processing, order confirmation, automatic cart clearing, and email notifications.

## ✨ Key Features

- 🔐 JWT-based user authentication
- 🛍️ Product browsing and cart management
- ➕ Add, remove and update cart quantities
- 📦 Automatic order creation
- 💳 Secure Stripe payment integration
- 🔔 Stripe webhook-based payment verification
- ✅ Automatic order status update after successful payment
- 🧹 Automatic cart clearing after payment
- 📧 Email confirmation after successful payment
- 🔎 Eureka service discovery
- 🚪 Centralized API Gateway
- ⚛️ Responsive React frontend
- 🗄️ MySQL database persistence

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Cloud
- Netflix Eureka
- Spring Cloud API Gateway
- JDBC / JdbcTemplate
- MySQL
- Maven
- Stripe Java SDK
- JavaMailSender

### Frontend
- React
- Vite
- Axios
- React Router
- Stripe React SDK

## Microservices

- Eureka Server
- API Gateway
- User Service
- Product Service
- Cart Service
- Order Service
- Payment Service
- Notification Service

## Architecture

React Frontend
      ↓
API Gateway
      ↓
┌─────────────┬──────────────┬─────────────┐
↓             ↓              ↓
User       Product         Cart
                              ↓
                           Order
                              ↓
                          Payment
                              ↓
                           Stripe
                              ↓
                           Webhook
                    ┌─────────┴─────────┐
                    ↓                   ↓
               Order → PAID       Cart → Cleared
                                        ↓
                               Notification Service
                                        ↓
                                  Gmail / Email




Envirornment Variables
======================

MYSQL_PASSWORD
JWT_SECRET
STRIPE_SECRET_KEY
STRIPE_WEBHOOK_SECRET
MAIL_USERNAME
MAIL_APP_PASSWORD
VITE_STRIPE_PUBLISHABLE_KEY 



Stripe Listen 
=============
stripe listen --forward-to http://localhost:8085/api/payments/webhook






## 📸 Application Screenshots

### Home Page
![PayCart Home Page](Docs/home.png)

### Products Page
![PayCart Products Page](Docs/products.png)

### Shopping Cart
![PayCart Shopping Cart](Docs/cart.png)

### Checkout Page
![PayCart Checkout Page](Docs/checkout.png)

### Stripe Secure Checkout
![Stripe Checkout](Docs/stripe-checkout.png)

### Payment Successful
![Payment Successful](Docs/payment-success.png)

## 🏗️ System Architecture

![PayCart Architecture](Docs/architecture.png) 