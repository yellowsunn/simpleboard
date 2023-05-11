rootProject.name = "simpleforum"

include(
    "board-monolithic-service",
    "apigateway-service",
    "user-service",
    "user-service:user-api",
    "user-service:user-core",
    "user-service:infra-persistence",
    "user-service:infra-aws-s3",
    "user-service:infra-redis",
    "user-service:infra-oauth2",
)
