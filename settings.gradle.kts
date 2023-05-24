rootProject.name = "simpleforum"

include(
    "board-monolithic-service",
    "apigateway-service",
    "common-library",

    "user-service",
    "user-service:user-api",
    "user-service:user-core",
    "user-service:infra-persistence",
    "user-service:infra-aws-s3",
    "user-service:infra-redis",
    "user-service:infra-oauth2",
    "user-service:infra-http",

    "board-service",
    "board-service:apps:board-api",
    "board-service:apps:board-kafka-consumer",
    "board-service:libs:board-core",
    "board-service:libs:infra-persistence",
    "board-service:libs:infra-http",
    "board-service:libs:infra-kafka-producer",
    "board-service:libs:infra-mongodb",
    "board-service:libs:infra-redis",
)
