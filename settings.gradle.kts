rootProject.name = "simpleforum"

include(
    "board-monolithic-service",
    "board-monolithic-service:common-library",
    "board-monolithic-service:common-persistence",

    "board-monolithic-service:user-service:user-api",
    "board-monolithic-service:user-service:user-core",
    "board-monolithic-service:user-service:infra-persistence",
)
