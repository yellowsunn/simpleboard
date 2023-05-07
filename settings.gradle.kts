rootProject.name = "simpleforum"

include(
    "board-monolithic-service",
    "apigateway-service",
    "user-service",
    "user-service:user-api",
    "user-service:user-core",
    "user-service:infra-persistence",
)
