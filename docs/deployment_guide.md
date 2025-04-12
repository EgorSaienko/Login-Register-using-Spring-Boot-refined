# 📦 Production Deployment Guide

This document provides step-by-step instructions for deploying the Equipment Inventory Management System in a production environment.

---

## ⚙️ 1. Hardware Requirements

| Component      | Minimum Requirement                     |
|----------------|------------------------------------------|
| Architecture   | x86_64 / amd64                          |
| CPU            | 2 cores (4+ recommended)                |
| RAM            | 4 GB (8+ GB recommended)                |
| Disk           | 10 GB free space (SSD recommended)      |
| Network        | Stable connection, open ports 8080, 3311 |

---

## 🧰 2. Required Software

- **Operating System**: Ubuntu 22.04 LTS (or any modern Linux distro)
- **Java Development Kit**: OpenJDK 17+
- **Maven**: 3.6+
- **MySQL Server**: 8.0+
- **Git**: latest version
- **Reverse Proxy (optional but recommended)**: Nginx
- **Process manager (optional)**: systemd / pm2 / supervisord

---

## 🌐 3. Network Configuration

- Ensure **port 8080** (Spring Boot server) and **3311** (MySQL) are open and not blocked by a firewall.
- If deploying publicly, consider using **HTTPS** via Nginx + Let’s Encrypt.
- Configure the reverse proxy (Nginx) to forward requests to port 8080.

---

## 🖥️ 4. Server Configuration

1. **Install Java 17**:
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```
2. **Install Maven:**
```bash
sudo apt install maven
```
3. **Install Git:**
```bash
sudo apt install git
```
4. **Clone the repository:**
```bash
git clone https://github.com/your-username/equipment-inventory.git
cd equipment-inventory
```

## 🗄️ 5. Database Configuration

1. Install MySQL:

```bash
sudo apt install mysql-server
```
```bash
Start MySQL service:
```

2. Start MySQL service:
```bash
sudo systemctl start mysql
sudo systemctl enable mysql
```

3. Log in to MySQL and create the database:
```sql
CREATE DATABASE inventory;
```

4. Create a user and grant privileges (optional but recommended):
```sql
CREATE USER 'inventory_user'@'localhost' IDENTIFIED BY 'strongpassword';
GRANT ALL PRIVILEGES ON inventory.* TO 'inventory_user'@'localhost';
FLUSH PRIVILEGES;
```

5. Configure application properties: Update the following lines in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3311/inventory?useSSL=false&serverTimezone=UTC
spring.datasource.username=inventory_user
spring.datasource.password=strongpassword
```

## 🚀 6. Deploying the Application

1. Build the project:
```bash
mvn clean install
```

2. Run the application:
```bash
java -jar target/equipment-inventory-0.0.1-SNAPSHOT.jar
```

3. Or configure systemd service (optional for persistent running): Create file `/etc/systemd/system/equipment.service`:
```ini
[Unit]
Description=Equipment Inventory App
After=network.target

[Service]
User=ubuntu
ExecStart=/usr/bin/java -jar /home/ubuntu/equipment-inventory/target/equipment-inventory-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Enable and start the service:
```bash
sudo systemctl daemon-reexec
sudo systemctl enable equipment
sudo systemctl start equipment
```

## ✅ 7. Verifying Deployment
- Open your browser and go to http://your-server-ip:8080
- You should see the login page.

Check the following:

- ✅ Login & registration pages accessible
- ✅ Able to register a new user
- ✅ Able to login with credentials
- ✅ Admin dashboard accessible (if role assigned in DB)
- ✅ Equipment CRUD and assignment working

Check logs for any exceptions:
```bash
journalctl -u equipment -f
```

Test endpoints with curl (optional):
```bash
curl http://localhost:8080/login
```

## 🧩 Optional: Reverse Proxy via Nginx

If you want the app to be available on port 80 or 443:
```bash
sudo apt install nginx
```

Sample Nginx config:
```nginx
server {
    listen 80;
    server_name yourdomain.com;

    location / {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

Restart Nginx:
```bash
sudo systemctl restart nginx
```

## 🔐 Security Recommendations

- Enable HTTPS with Let's Encrypt (Certbot).
- Move database credentials to environment variables or external config.
- Consider using Docker + Docker Compose for reproducible deployments.
- Monitor with tools like Prometheus + Grafana.

## 📞 Support
For issues with deployment, contact the developer or create an issue on GitHub. 
[GitHub Repository](https://github.com/EgorSaienko/Login-Register-using-Spring-Boot-refined.git)
