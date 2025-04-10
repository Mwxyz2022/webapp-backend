plugins {
    id("java")
    id("org.springframework.boot") version "3.4.4" apply false
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
    // ⬅️ ВАЖЛИВО: спочатку BOM Spring Boot
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.4.4"))

    // Валідація
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Логування
    implementation("org.slf4j:slf4j-api:2.0.9")

    // Spring Web (щоб працював RestControllerAdvice)
    implementation("org.springframework.boot:spring-boot-starter-web")
}