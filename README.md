## AI-Based Farmer Query Support and Advisory System

Helping farmers make informed decisions through AI-powered crop guidance, weather insights, and smart advisory services.

---

## 🌾 About the Project

The **AI-Based Farmer Query Support and Advisory System** is a full-stack web application designed to support farmers by providing instant agricultural guidance. The platform combines artificial intelligence, weather information, and secure user management to deliver practical recommendations for crop care and farming practices.

This project was developed as an academic full-stack application using **Spring Boot**, **React**, and **MongoDB**.

---

## 🚀 Key Features

- 🔐 Secure User Authentication using JWT
- 🤖 AI-powered Farmer Query Assistant
- 🌦 Live Weather Information
- 🌱 Crop Advisory and Farming Tips
- 📷 Crop Image Upload
- 💬 Chat History Management
- 👤 User Profile Management
- 📧 Forgot Password with OTP Verification

---

## 🛠 Technology Stack

### Frontend
*   **Library:** React.js (via Vite)
*   **Language:** JavaScript (ES6+)
*   **Styling:** HTML5, CSS3, Responsive Design layouts

### Backend
*   **Framework:** Spring Boot (Java)
*   **Security:** Spring Security, JWT Authentication
*   **Build Tool:** Maven

### Database & APIs
*   **Primary Database:** MongoDB Atlas (Cloud NoSQL)
*   **Integrations:** OpenWeatherMap API (or custom Weather API Provider), JavaMailSender (SMTP)

---
---

## 📁 Project Structure

AI-Based-Farmer-Query-Support-and-Advisory-System
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/farmer/advisory/      # Controller, Service, Repository layers
│   │   │   └── resources/                     # application.properties, email templates
│   └── pom.xml                                # Maven dependencies
│
├── frontend/
│   ├── src/
│   │   ├── components/                        # Reusable UI elements (Navbar, Sidebar, etc.)
│   │   ├── pages/                             # Dashboard, Chat, Profile, Login/Signup
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json                           # Node.js dependencies
│   └── vite.config.js                         # Vite configuration
│
└── README.md

---

## ⚙️ Getting Started

 Run the Backend System

```bash
cd backend
mvn spring-boot:run
```

Run the Frontend System

```bash
cd frontend
npm install
npm run dev
```

---

## 🔑 Required Environment Variables

Before running the project, configure the following variables:

```
# Server Setup
server.port=8080

# Database Connectivity
spring.data.mongodb.uri=your_mongodb_atlas_connection_string

# Authentication Configurations
jwt.secret=your_custom_secure_base64_jwt_secret_key

# Third Party External APIs
WEATHER_API_KEY=your_weather_api_provider_key

# Mail Service Settings
spring.mail.username=your_email@domain.com
spring.mail.password=your_app_specific_password_keys
```

## 🌾 Note

This system is designed to support farmers by providing AI-assisted crop guidance and agricultural recommendations. The advisory should be used as a supportive tool and not as a replacement for professional agricultural consultation.
> **"Empowering agriculture through technology, one informed decision at a time."** 🌱
