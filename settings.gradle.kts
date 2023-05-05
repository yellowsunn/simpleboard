rootProject.name = "simpleforum"

include(
    "board-monolithic-service",
    "board-monolithic-service:common-library",
    "board-monolithic-service:common-persistence",

    "board-monolithic-service:user-service:user-api",
    "board-monolithic-service:user-service:user-core",
    "board-monolithic-service:user-service:infra-persistence",

    "board-monolithic-service:board-service:board-api",
    "board-monolithic-service:board-service:board-core",
    "board-monolithic-service:board-service:infra-persistence",
)
