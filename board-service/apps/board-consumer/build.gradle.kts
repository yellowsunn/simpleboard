import com.google.cloud.tools.jib.gradle.JibExtension

plugins {
    id("com.google.cloud.tools.jib") version BoardServiceVersions.jib
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation(project(":common-library"))
    implementation(project(":board-service:libs:board-core"))

    runtimeOnly(project(":board-service:libs:infra-kafka-producer"))
    runtimeOnly(project(":board-service:libs:infra-http"))
    runtimeOnly(project(":board-service:libs:infra-persistence"))
    runtimeOnly(project(":board-service:libs:infra-mongodb"))
    runtimeOnly(project(":board-service:libs:infra-redis"))
}

configure<JibExtension> {
    from {
        image = "eclipse-temurin:17-jre-alpine"
    }
    container {
        ports = listOf("8080")
    }
}
