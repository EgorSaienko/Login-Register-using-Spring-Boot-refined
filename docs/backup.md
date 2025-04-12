# 🛡️ Backup Strategy for Equipment Inventory Management System

Цей документ описує повну стратегію резервного копіювання для захисту даних і можливості відновлення системи у випадку збою або втрати інформації.

---

## 🧠 1. Backup Strategy

- Захист бази даних, конфігурацій, користувацьких файлів і логів.
- Комбіноване використання повних та інкрементальних резервних копій.
- Автоматизація процесу за допомогою Bash-скриптів або інструментів (наприклад, `cron`, `rsnapshot`, `borg`, `restic`).

---

## 📦 2. Types of Backups

| Тип              | Опис                                                                 |
|------------------|----------------------------------------------------------------------|
| Повна (Full)     | Повна копія всіх критичних даних. Виконується щотижня.              |
| Інкрементальна   | Зберігає лише зміни з моменту останньої копії (повної або інкрементальної). Виконується щодня. |
| Диференціальна   | Зберігає зміни з моменту останньої повної копії. Використовується рідко. |


## 🗓️ 3. Backup Frequency

- **Повна копія**: щотижня (неділя, 03:00).
- **Інкрементальна копія**: щодня (03:00).
- **Перевірка цілісності**: щотижня після повної копії.

---

## 🗃️ 4. Storage & Rotation

- Локальне сховище: `/backups/`
- Віддалене сховище: SFTP / AWS S3 / Google Cloud Storage
- **Ротація**:
    - Повні копії: зберігати 4 тижні.
    - Інкрементальні копії: зберігати 7 днів.
- Використовуй `logrotate` або `backup rotation scripts`.

---

## 🔄 5. Backup Procedure

### 5.1. Database (MySQL)

```bash
# backup_db.sh
#!/bin/bash
TIMESTAMP=$(date +"%F")
BACKUP_DIR="/backups/db"
DB_NAME="inventory"
DB_USER="inventory_user"
DB_PASS="strongpassword"

mkdir -p $BACKUP_DIR
mysqldump -u $DB_USER -p$DB_PASS $DB_NAME > $BACKUP_DIR/db_$TIMESTAMP.sql
```

### 5.2. Configuration Files

```bash
tar czf /backups/configs/configs_$(date +%F).tar.gz /home/ubuntu/equipment-inventory/src/main/resources/application.properties
```

### 5.3. User Data (if uploaded images, etc.)

```bash
tar czf /backups/userdata/userdata_$(date +%F).tar.gz /home/ubuntu/equipment-inventory/uploads
```

### 5.4. Logs

```bash
tar czf /backups/logs/logs_$(date +%F).tar.gz /var/log/equipment/
```

## 🔍 6. Integrity Verification

- Перевірка архівів:
```bash
tar -tzf /backups/db/db_2025-04-12.sql.gz > /dev/null && echo "Valid"
```
- Для бази: спроба імпорту в тестову БД:
```bash
mysql -u root -p test_db < /backups/db/db_2025-04-12.sql
```

## 🤖 7. Automation Tools

- Використовуй cron для планування:
```bash
# /etc/crontab
0 3 * * 0 root /scripts/backup_full.sh
0 3 * * 1-6 root /scripts/backup_incremental.sh
```
- Або інструменти:
- rsnapshot
- borg
- restic

## 🛠️ 8. Recovery Procedures

### 8.1. Full System Recovery

1. Встановити MySQL, Java, Maven.
2. Розгорнути код з Git.
3. Відновити базу:
```bash
mysql -u root -p inventory < /backups/db/db_2025-04-12.sql
```
4. Відновити конфіги:
```bash
cp /backups/configs/configs_2025-04-12.tar.gz /project/resources/
```
5. Запустити додаток:
```bash
java -jar target/equipment-inventory-0.0.1-SNAPSHOT.jar
```

### 8.2. Selective Restore

- Відновити лише один файл або директорію:
```bash
tar -xzf /backups/userdata/userdata_2025-04-12.tar.gz -C /home/ubuntu/equipment-inventory/uploads
```

## 🧪 9. Recovery Testing

- Щомісячне відновлення у тестовому середовищі.
- Перевірка логіну, відображення даних, створення нових записів.
- Перевірка даних у БД (таблиці, цілісність foreign keys).

## 📞 Support
Для питань — звертайтеся до DevOps-команди або відкрийте issue у GitHub.
[GitHub Repository](https://github.com/EgorSaienko/Login-Register-using-Spring-Boot-refined.git)