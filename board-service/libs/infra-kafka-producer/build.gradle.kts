dependencies {
    implementation(project(":board-service:libs:board-core"))
    implementation(project(":common-library"))
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
}
