build:
	docker build -f Dockerfile -t auth-service:latest .

run:
	docker run -p 8080:8080 --env SPRING_PROFILES_ACTIVE=dev auth-service:latest

test:
	../../gradlew :services:auth-service:test

clean:
	docker rmi auth-service:latest || true
