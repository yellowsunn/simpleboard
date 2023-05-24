dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation(project(":common-library"))
    implementation(project(":board-service:libs:board-core"))

    runtimeOnly(project(":board-service:libs:infra-kafka-producer"))
    runtimeOnly(project(":board-service:libs:infra-http"))
    runtimeOnly(project(":board-service:libs:infra-persistence"))
    runtimeOnly(project(":board-service:libs:infra-mongodb"))
    runtimeOnly(project(":board-service:libs:infra-redis"))
}
