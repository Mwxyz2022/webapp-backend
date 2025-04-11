# 🛠 Збірка всіх сервісів
build:
	docker compose build

# 🚀 Запуск усіх сервісів у dev-режимі з Redis/PostgreSQL
up:
	docker compose up --build

# 🧹 Зупинка і очищення volume
clean:
	docker compose down -v

# 🧪 Запуск усіх тестів
test:
	./gradlew clean test

# 🔍 Перегляд логів
logs:
	docker compose logs -f --tail=100

# 📦 Білд тільки auth-service
build-auth:
	docker compose build auth-service

# 📦 Білд тільки user-service
build-user:
	docker compose build user-service
