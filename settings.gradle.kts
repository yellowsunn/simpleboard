rootProject.name = "simpleforum"

include(
    "board-monolithic-service",
    "user-service",
    "user-service:user-api",
    "user-service:user-core",
)
