#!/bin/bash

DB_NAME="inventory"
DB_USER="inventory_user"
DB_PASS="strongpassword"
BACKUP_FILE="/backups/full/2025-04-12/db/db_2025-04-12.sql"

mysql -u $DB_USER -p$DB_PASS $DB_NAME < $BACKUP_FILE

echo "✅ Database restored from $BACKUP_FILE"
