package com.example.demo.model;

/**
 * Ролі користувачів системи.
 */
public enum Role {
    /** Звичайний користувач. */
    USER("User"),

    /** Адміністратор системи. */
    ADMIN("Admin");

    private final String value;

    /**
     * Конструктор ролі.
     * @param value відображуване значення ролі
     */
    Role(String value) {
        this.value = value;
    }

    /**
     * Повертає відображуване значення ролі.
     * @return значення ролі
     */
    public String getValue() {
        return value;
    }
}
