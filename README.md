# Equipment Inventory Management System

This is a web-based inventory management application built using Spring Boot. It supports assigning equipment to users, tracking statuses, adding comments, and managing the entire equipment lifecycle. It also includes login and registration with role-based access control.

## 🔧 Technologies Used

- Java 17+
- Spring Boot
- Spring Security (SecurityFilterChain)
- JPA + Hibernate
- MySQL
- Thymeleaf
- Maven

## 👥 Roles

- `ADMIN` – can add, edit, assign, and delete equipment.
- `USER` – can view their assigned equipment and leave comments.

## 📁 Project Structure

- `controller/` – contains web controllers for admin and user views.
- `service/` – business logic for equipment and user management.
- `model/` – entity classes for users, equipment, statuses, and comments.
- `repository/` – Spring Data JPA interfaces for data access.
- `templates/` – Thymeleaf-based HTML templates.
- `application.properties` – contains server and database configuration.

## 💡 Project Logic

The system enables:
- Admins to register equipment and assign it to users.
- Users to view their equipment and provide feedback (comments).
- Admins to track condition and status of equipment (`NEW`, `WORN`, `DAMAGED`, etc.).
- Dynamic user search for assignment using AJAX.

## 🚀 Future Use

This system will evolve into a server monitoring solution. Future features may include:
- Server resource usage tracking
- Real-time process monitoring
- Notifications and analytics dashboard

## 📄 License

This project is licensed under the [MIT License](LICENSE).

## ⚙️ Configuration

You can modify database or server settings in the `src/main/resources/application.properties` file.

```properties
#-------------------- Server properties ------------------
server.port=8080
server.error.include-message=always

#-------------------- Logging ----------------------------
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type=TRACE
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=ERROR
spring.thymeleaf.cache=false

#-------------------- DB Connection ----------------------
spring.datasource.url=jdbc:mysql://localhost:3311/inventory?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

#-------------------- JPA / Hibernate ---------------------
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.jpa.properties.hibernate.format_sql=true
