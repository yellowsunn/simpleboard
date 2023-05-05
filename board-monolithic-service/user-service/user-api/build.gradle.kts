dependencies {
    implementation(project(":board-monolithic-service:user-service:user-core"))
    implementation(project(":board-monolithic-service:common-library"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.data:spring-data-commons")

    runtimeOnly(project(":board-monolithic-service:user-service:infra-persistence"))
}
