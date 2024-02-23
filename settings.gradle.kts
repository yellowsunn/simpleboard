rootProject.name = "simpleforum"

include(
    "common-library",

    "apigateway-service",

    "image-service",

    "user-service:apps:user-api",

    "board-service:apps:board-api",
    "board-service:apps:board-consumer",
    "board-service:apps:board-batch",
    "board-service:libs:board-core",
    "board-service:libs:infra-persistence",
    "board-service:libs:infra-http",
    "board-service:libs:infra-kafka-producer",
    "board-service:libs:infra-mongodb",
    "board-service:libs:infra-redis",

    "notification-service:apps:notification-api",
    "notification-service:apps:notification-consumer",
    "notification-service:libs:notification-core",
    "notification-service:libs:infra-http",
    "notification-service:libs:infra-mongodb",
)
