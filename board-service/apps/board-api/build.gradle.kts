dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(project(":common-library"))
    implementation(project(":board-service:libs:board-core"))

    runtimeOnly(project(":board-service:libs:infra-persistence"))
    runtimeOnly(project(":board-service:libs:infra-http"))
    runtimeOnly(project(":board-service:libs:infra-kafka-producer"))
}
