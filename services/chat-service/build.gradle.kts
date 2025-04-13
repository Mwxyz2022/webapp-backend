plugins {
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Спільні DTO + Kafka Events
    implementation(project(":common"))
    implementation(project(":infrastructure:kafka-config"))
    implementation(project(":infrastructure:redis-config"))

    // REST + WebSocket
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")


    // MongoDB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    // Валідація
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Логування
    implementation("org.slf4j:slf4j-api:2.0.9")

    // Тестування
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
