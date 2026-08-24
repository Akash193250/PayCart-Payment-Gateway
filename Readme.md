# Payment Integration Gateway

A full-stack e-commerce payment platform built using Java, Spring Boot, Microservices, React, MySQL and Stripe.

## Features

- User registration and login
- JWT-based authentication and authorization
- Product catalogue
- Shopping cart management
- Order creation and tracking
- Stripe PaymentIntent integration
- Stripe webhook processing
- Automatic order status update
- Automatic cart clearing after successful payment
- Email confirmation using Gmail SMTP
- Eureka service discovery
- API Gateway routing
- Swagger/OpenAPI documentation
- Environment-variable based secret management
- Global exception handling

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






## Application Screenshots

### Architecture
![PayCart Architecture](docs/architecture.png)

### Home
![Home](docs/home.png)

### Products
![Products](docs/products.png)

### Cart
![Cart](docs/cart.png)

### Checkout
![Checkout](docs/checkout.png)

### Payment Success
![Payment Success](docs/payment-success.png)