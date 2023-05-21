dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(project(":common-library"))
    implementation(project(":board-service:board-core"))

    runtimeOnly(project(":board-service:infra-persistence"))
    runtimeOnly(project(":board-service:infra-http"))
    runtimeOnly(project(":board-service:infra-kafka-producer"))
}
