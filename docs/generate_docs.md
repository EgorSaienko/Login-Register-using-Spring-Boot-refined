# Генерація документації за допомогою Javadoc

## Варіант 1: Через командний рядок

1. Відкрийте термінал у кореневій директорії проєкту.
2. Виконайте команду (залежно від вашої структури проєкту):

```bash
javadoc -d docs/api -sourcepath src/main/java -subpackages com.example.demo

```

> **Пояснення параметрів:**
> - `-d docs/api` — шлях до директорії, куди буде згенеровано документацію.
> - `-sourcepath src` — шлях до директорії з Java-кодом.
> - `-subpackages com.yourpackage` — назва вашого головного пакету.

3. Після генерації, відкрийте файл `docs/api/index.html` у браузері.

---

## Варіант 2: У середовищі IntelliJ IDEA

1. Відкрийте свій проєкт у IntelliJ IDEA.
2. У верхньому меню оберіть **Tools > Generate JavaDoc...**
3. У вікні, що з'явиться:
   - Виберіть модуль або пакет.
   - Вкажіть директорію для виводу, наприклад `docs/api`.
   - Увімкніть галочку "Include protected and public members".
4. Натисніть **OK** для запуску генерації.

---

## Створення архіву з документацією

Після успішної генерації документації, виконайте команду:

```bash
cd docs/api
zip -r java_docs.zip .
