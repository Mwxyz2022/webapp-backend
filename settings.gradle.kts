rootProject.name = "webapp-backend"

include("common")
project(":common").projectDir = file("common")

include("services:auth-service")
project(":services:auth-service").projectDir = file("services/auth-service")
include("services:user-service")
project(":services:user-service").projectDir = file("services/user-service")


include("infrastructure:redis-config")
project(":infrastructure:redis-config").projectDir = file("infrastructure/redis-config")
include("infrastructure:kafka-config")
project(":infrastructure:kafka-config").projectDir  = file("infrastructure/kafka-config")
