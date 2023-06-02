dependencies {
    implementation(project(":common-library"))
    implementation(project(":notification-service:libs:notification-core"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    runtimeOnly(project(":notification-service:libs:infra-mongodb"))
}
