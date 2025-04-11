package com.example.demo.model;

/**
 * Перелік можливих статусів обладнання.
 */
public enum EquipmentStatus {
    /** Нове обладнання. */
    NEW("New"),

    /** У хорошому стані. */
    GOOD_CONDITION("Good Condition"),

    /** Зношене обладнання. */
    WORN("Worn"),

    /** Потребує ремонту. */
    NEEDS_REPAIR("Needs Repair"),

    /** Пошкоджене. */
    DAMAGED("Damaged");

    private final String displayValue;

    /**
     * Конструктор для зберігання відображуваного значення.
     * @param displayValue текст для відображення
     */
    EquipmentStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * Повертає значення для відображення (UI).
     * @return відображуване значення
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Повертає значення, яке зберігається в базі даних.
     * @return значення enum у форматі бази даних
     */
    public String getDatabaseValue() {
        return name();
    }

    /**
     * Перетворює рядок з бази даних у відповідний enum.
     * @param dbValue значення з бази даних
     * @return відповідний елемент enum
     */
    public static EquipmentStatus fromDatabaseValue(String dbValue) {
        return EquipmentStatus.valueOf(dbValue);
    }
}