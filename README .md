# 🧱 webapp-backend — Архітектура мікросервісів на Spring Boot

Проєкт реалізує мікросервісну архітектуру для онлайн-магазину з Telegram WebApp авторизацією, Redis, PostgreSQL, Kafka та Docker.

---

## 📦 Структура модулів

```
webapp-backend/
├── common/                     # DTO, утиліти, глобальні конфіги
│   └── config/
│       ├── CommonConfig.java
│       └── EnableCommonModule.java
│
├── infrastructure/
│   └── redis-config/          # Redis конфігурація
│       └── config/
│           ├── RedisConfig.java
│           └── EnableRedisModule.java
│
├── services/
│   ├── auth-service/          # Авторизація, Telegram WebApp, Redis + JWT
│   │   ├── client/
│   │   │   ├── UserClient.java
│   │   │   └── config/EnableUserClient.java
│   │   └── AuthApplication.java
│   │
│   └── user-service/          # CRUD користувача + PostgreSQL
│       └── UserServiceApplication.java
```

---

## 🧩 Як підключати модулі правильно

### ✅ 1. Підключення `common`

1. Створити `CommonConfig + EnableCommonModule` (вже є в `common/config`)
2. У своєму сервісі (`AuthApplication` тощо):

```java
@EnableCommonModule
```

---

### ✅ 2. Підключення Redis

1. Є `RedisConfig + EnableRedisModule` в `redis-config/config`
2. У сервісі:

```java
@EnableRedisModule
```

---

### ✅ 3. Підключення Feign клієнтів

1. У `auth-service/client/config`:
```java
@EnableFeignClients(basePackages = "com.example.auth.client")
```

2. Інкапсулюємо в `@EnableUserClient`, і в main класі сервісу:

```java
@EnableUserClient
```

---

## 🚀 Запуск проєкту (DEV)

1. Встанови Docker
2. У корені проєкту:
```bash
make up
```

3. Запустяться сервіси:
   - auth-service (порт 8080)
   - user-service (порт 8086)

> Redis і PostgreSQL піднімаються окремо, але на одному `bridge`-мережевому інтерфейсі

---

## ✅ Принципи архітектури

- Мінімальні залежності в кожному модулі
- Кожен модуль сам себе сканує (через `@Enable*`)
- Контейнери зібрані тільки з потрібним кодом
- Dockerfile = multi-stage + distroless образи
- Повна підтримка `dev` і `prod` профілів
- Проста інтеграція з Telegram WebApp

---

## 🧠 Автор

🛠️ Архітектура складена з урахуванням production-best-practices:
- Spring Boot 3.4.4 + Java 21
- Redis + PostgreSQL
- Kafka-ready структура
- Gradle Kotlin DSL
- Minimal Docker build (distroless)

---

📂 Далі: KafkaConfig, Gateway, Monitoring, Test Coverage — готові до інтеграції.