build:
	docker build -f Dockerfile -t user-service:latest .

run:
	docker run -p 8086:8086 --env SPRING_PROFILES_ACTIVE=dev user-service:latest

test:
	../../gradlew :services:user-service:test

clean:
	docker rmi user-service:latest || true
