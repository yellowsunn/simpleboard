dependencies {
    implementation(project(":board-monolithic-service:common-library"))
    implementation(project(":board-monolithic-service:board-service:board-core"))

    runtimeOnly(project(":board-monolithic-service:board-service:infra-persistence"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.data:spring-data-commons")

    // TODO: 삭제
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}
