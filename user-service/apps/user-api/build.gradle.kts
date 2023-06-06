dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.session:spring-session-data-redis")

    implementation(project(":common-library"))
    implementation(project(":user-service:libs:user-core"))
    runtimeOnly(project(":user-service:libs:infra-persistence"))
    runtimeOnly(project(":user-service:libs:infra-redis"))
    runtimeOnly(project(":user-service:libs:infra-http"))
}
