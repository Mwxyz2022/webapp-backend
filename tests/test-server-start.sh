#!/bin/bash

PORT=9133
PROXY_PORT=1337
FOLDER=~/webapp-backend/tests/chat
PROXY_PATH="/home/fua/.nvm/versions/node/v18.20.8/lib/node_modules/cors-anywhere/server.js"

# 🔪 Завершуємо процеси, що займають порти
for p in "$PORT" "$PROXY_PORT"; do
  if fuser -s "$p"/tcp; then
    echo "⚠️ Порт $p зайнятий. Завершуємо..."
    fuser -k "$p"/tcp
    echo "⏳ Очікуємо звільнення порту $p..."
    while fuser -s "$p"/tcp; do
      sleep 0.5
    done
  fi
done

# 📁 Переходимо в папку з HTML-тестами
cd "$FOLDER" || exit 1

# 🧹 Обробник Ctrl+C — завершення обох процесів
trap 'echo -e "\n🛑 Зупинка..."; fuser -k "$PORT"/tcp "$PROXY_PORT"/tcp 2>/dev/null; exit 0' INT

# ▶️ Запуск CORS-проксі
echo "🚀 Запускаємо проксі на http://localhost:$PROXY_PORT/"
PORT=$PROXY_PORT node "$PROXY_PATH" &

# ▶️ Запуск локального HTTP-сервера
echo "🌐 Запускаємо HTTP сервер на http://localhost:$PORT/"
echo "🛑 Щоб зупинити обидва сервери — натисни Ctrl+C"

# 🟢 Старт
exec python3 -m http.server "$PORT" --bind 127.0.0.1
