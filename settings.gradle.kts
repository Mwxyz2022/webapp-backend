rootProject.name = "webapp-backend"

include("common")
project(":common").projectDir = file("common")

include("services:auth-service")
project(":services:auth-service").projectDir = file("services/auth-service")