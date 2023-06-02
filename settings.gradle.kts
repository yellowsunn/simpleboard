rootProject.name = "simpleforum"

include(
    "board-monolithic-service",
    "apigateway-service",
    "common-library",

    "image-service",

    "user-service",
    "user-service:apps:user-api",
    "user-service:libs:user-core",
    "user-service:libs:infra-persistence",
    "user-service:libs:infra-redis",
    "user-service:libs:infra-oauth2",
    "user-service:libs:infra-http",

    "board-service",
    "board-service:apps:board-api",
    "board-service:apps:board-kafka-consumer",
    "board-service:libs:board-core",
    "board-service:libs:infra-persistence",
    "board-service:libs:infra-http",
    "board-service:libs:infra-kafka-producer",
    "board-service:libs:infra-mongodb",
    "board-service:libs:infra-redis",

    "notification-service",
    "notification-service:apps:notification-consumer",
    "notification-service:libs:notification-core",
    "notification-service:libs:infra-http",
    "notification-service:libs:infra-mongodb",
)
