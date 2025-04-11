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

## Code Documentation

This project uses the **Javadoc** standard. All public methods, classes, and interfaces must be documented.

**Guidelines:**
- Use Javadoc comments `/** ... */` above each public element.
- Always describe parameters and return values using the `@param` and `@return` tags.
- If the method may throw exceptions — include the `@throws` tag.

Documentation is automatically generated using the JDK `javadoc` command.

For more details, see [`docs/generate_docs.md`](docs/generate_docs.md).

### Generating documentation using Javadoc in IntelliJ IDEA

To generate the documentation in IntelliJ IDEA:

1. Go to the menu: **Tools > Generate JavaDoc...**
2. In the popup window:
    - Select the module or package you want to document.
    - Set the **Output directory**, e.g., `docs/api`
    - Make sure the **scope** is set to include public elements.
3. Click **OK**.
4. Open the `docs/api/index.html` file in your browser to view the documentation.

To archive the generated documentation:
```bash
cd docs/api
zip -r java_docs.zip .

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

