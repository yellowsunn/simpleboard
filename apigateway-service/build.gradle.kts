import com.google.cloud.tools.jib.gradle.JibExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version ApiGatewayVersions.springBoot
    id("io.spring.dependency-management") version ApiGatewayVersions.springDependencyManagement
    id("com.google.cloud.tools.jib") version ApiGatewayVersions.jib
    kotlin("jvm") version ApiGatewayVersions.kotlin
    kotlin("plugin.spring") version ApiGatewayVersions.kotlin
}

group = "com.yellowsunn"
version = "0.0.1-SNAPSHOT"

extra["springCloudVersion"] = "2022.0.2"

dependencies {
    implementation(project(":common-library"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configure<JibExtension> {
    from {
        image = "eclipse-temurin:17-jre-alpine"
    }
    container {
        ports = listOf("8080")
    }
}
