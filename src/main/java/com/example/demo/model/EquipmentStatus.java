package com.example.demo.model;

public enum EquipmentStatus {
    NEW("New"),
    GOOD_CONDITION("Good Condition"),
    WORN("Worn"),
    NEEDS_REPAIR("Needs Repair"),
    DAMAGED("Damaged");

    private final String displayValue;

    // Конструктор для зберігання відображуваного значення
    EquipmentStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    // Метод для отримання значення для відображення
    public String getDisplayValue() {
        return displayValue;
    }

    // Метод для отримання значення для бази даних
    public String getDatabaseValue() {
        return name(); // Повертає значення в форматі, який зберігається в базі даних (наприклад, GOOD_CONDITION)
    }

    // Метод для отримання enum за значенням, що зберігається в базі даних
    public static EquipmentStatus fromDatabaseValue(String dbValue) {
        return EquipmentStatus.valueOf(dbValue); // Перетворення з рядка в enum
    }
}
