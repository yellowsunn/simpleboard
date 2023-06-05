dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(project(":common-library"))
    implementation(project(":notification-service:libs:notification-core"))

    runtimeOnly(project(":notification-service:libs:infra-mongodb"))
}
