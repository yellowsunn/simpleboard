import com.google.cloud.tools.jib.gradle.JibExtension

plugins {
    id("com.google.cloud.tools.jib") version BoardServiceVersions.jib
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation(project(":common-library"))
    implementation(project(":board-service:libs:board-core"))

    runtimeOnly(project(":board-service:libs:infra-kafka-producer"))
    runtimeOnly(project(":board-service:libs:infra-persistence"))
    runtimeOnly(project(":board-service:libs:infra-redis"))
    runtimeOnly("com.mysql:mysql-connector-j")
}

configure<JibExtension> {
    from {
        image = "eclipse-temurin:17-jre-alpine"
    }
    container {
        ports = listOf("8080")
    }
}
