#!/bin/bash

# Підключаємо змінні з конфіг-файлу
source "$(dirname "$0")/../config/backup.conf"

TIMESTAMP=$(date +"%F")
BACKUP_DIR="/backups/full/$TIMESTAMP"
CONFIG_FILE="./src/main/resources/application.properties"
USER_DATA_DIR="./uploads"
LOG_DIR="/var/log/equipment"

mkdir -p "$BACKUP_DIR/db" "$BACKUP_DIR/configs" "$BACKUP_DIR/userdata" "$BACKUP_DIR/logs"

mysqldump -u "$DB_USER" -p"$DB_PASS" "$DB_NAME" > "$BACKUP_DIR/db/db_$TIMESTAMP.sql"
cp "$CONFIG_FILE" "$BACKUP_DIR/configs/"
tar -czf "$BACKUP_DIR/userdata/userdata_$TIMESTAMP.tar.gz" -C "$USER_DATA_DIR" .
tar -czf "$BACKUP_DIR/logs/logs_$TIMESTAMP.tar.gz" -C "$LOG_DIR" .

echo "✅ Full backup completed at $BACKUP_DIR"
