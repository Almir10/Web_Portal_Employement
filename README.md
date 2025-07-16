# Web_Portal_Employment

A full-stack web application built with Spring Boot and Thymeleaf, designed to connect **junior developers** with **employers** offering entry-level job opportunities.

## ✨ Features

- Employer registration, login, job posting, and application management
- Developer registration, login, profile management, and job application
- Admin dashboard for managing users and job posts
- Role-based access control with Spring Security
- Clean MVC architecture with layered backend (Controller, Service, Repository)
- MySQL database integration
- Intuitive Thymeleaf-based frontend

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot, Spring Security  
- **Frontend:** Thymeleaf, HTML/CSS  
- **Database:** MySQL  
- **Architecture:** MVC, layered project structure



## ⚙️ Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/Almir10/Web_Portal_Employment.git
   cd Web_Portal_Employment
   ```

2. **Set up the database**

   Create a MySQL database named `web_portal` (or use a different name and update the config).

3. **Configure `application.properties`**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/web_portal
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.thymeleaf.cache=false
   ```

4. **Run the application**

   - Via IDE
   - Or via terminal:
     ```bash
     ./mvnw spring-boot:run
     ```

5. **Access the app**

   Open your browser and go to:
   ```
   http://localhost:8080
   ```
