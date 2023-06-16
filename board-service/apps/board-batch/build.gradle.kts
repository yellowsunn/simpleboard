dependencies {
    implementation(project(":board-service:libs:board-core"))

    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly(project(":board-service:libs:infra-persistence"))
    runtimeOnly(project(":board-service:libs:infra-http"))
    runtimeOnly(project(":board-service:libs:infra-kafka-producer"))
    runtimeOnly(project(":board-service:libs:infra-mongodb"))
    runtimeOnly(project(":board-service:libs:infra-redis"))
}
