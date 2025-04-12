#!/bin/bash

TIMESTAMP=$(date +"%F")
BACKUP_DIR="/backups/incremental/$TIMESTAMP"
USER_DATA_DIR="/home/ubuntu/equipment-inventory/uploads"
LOG_DIR="/var/log/equipment"

mkdir -p "$BACKUP_DIR/userdata" "$BACKUP_DIR/logs"

# === BACKUP CHANGED FILES (last 24h) ===
find $USER_DATA_DIR -type f -mtime -1 -print0 | tar -czf "$BACKUP_DIR/userdata/inc_userdata_$TIMESTAMP.tar.gz" --null -T -
find $LOG_DIR -type f -mtime -1 -print0 | tar -czf "$BACKUP_DIR/logs/inc_logs_$TIMESTAMP.tar.gz" --null -T -

echo "✅ Incremental backup completed at $BACKUP_DIR"
