# 🧱 WebApp Backend (Java + Spring Boot + Microservices)

Це бекенд для онлайн-магазину з Telegram авторизацією, реалізований на основі:
- Java 21
- Spring Boot 3.4.4
- Gradle (Kotlin DSL)
- Redis, PostgreSQL
- Docker + Docker Compose
- Kafka-ready
- Мікросервісна архітектура з модулями: `auth-service`, `user-service`, `common`, `infrastructure`

---

## 📁 Структура проєкту

```
webapp-backend/
├── common/                        # DTO, утиліти, помилки
├── infrastructure/
│   └── redis-config/             # Redis конфіг
├── services/
│   ├── auth-service/             # Telegram авторизація
│   └── user-service/             # Збереження користувачів
├── docker-compose.yml            # Production-середовище
├── docker-compose.override.yml   # Dev-середовище з Redis/Postgres
├── .env                          # Змінні середовища
├── Makefile                      # Універсальні dev-команди
```

---

## 🚀 Швидкий старт

1. ⚙️ Створити `.env`:

```env
JWT_SECRET=your_dev_jwt_secret
TELEGRAM_BOT_TOKEN=your_dev_bot_token
REDIS_PASSWORD=password
POSTGRES_PASSWORD=password
```

2. ▶️ Запуск усіх сервісів у dev-режимі:

```bash
make up
```

3. ⛔ Зупинка + очищення:

```bash
make clean
```

---

## 🐳 Команди

| Команда          | Опис                                |
|------------------|--------------------------------------|
| `make build`     | Збірка всіх сервісів                 |
| `make up`        | Запуск сервісів + Redis/Postgres     |
| `make clean`     | Зупинка і видалення volume           |
| `make test`      | Запуск юніт-тестів через Gradle      |
| `make logs`      | Перегляд логів усіх контейнерів      |
| `make build-auth`| Білд тільки auth-service             |
| `make build-user`| Білд тільки user-service             |

---

## 🧪 Тести

```bash
make test
```

---

## 🔐 Сервіси

| Сервіс        | Порт | Призначення                   |
|---------------|------|-------------------------------|
| auth-service  | 8080 | Telegram login + JWT          |
| user-service  | 8086 | Збереження Telegram-користувачів |
| redis         | 6379 | Redis для токенів             |
| postgres      | 5433 | База для user-service         |

---

## 📄 Профілі

- `dev` — з `application-dev.yml`
- `prod` — через `SPRING_PROFILES_ACTIVE=prod`

---

## 🧱 Build образів

```bash
docker build -f services/auth-service/Dockerfile -t auth-service:latest .
docker build -f services/user-service/Dockerfile -t user-service:latest .
```

---

## ✅ Done!
Готово до запуску в Docker, CI/CD або Kubernetes 💪
