dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

//    implementation(project(":board-service:libs:infra-kafka-producer"))

    runtimeOnly("com.mysql:mysql-connector-j")
}
