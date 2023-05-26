import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version ImageServiceVersions.springBoot
    id("io.spring.dependency-management") version ImageServiceVersions.springDependencyManagement
    kotlin("jvm") version ImageServiceVersions.kotlin
    kotlin("plugin.spring") version ImageServiceVersions.kotlin
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":common-library"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("commons-io:commons-io:${UserServiceVersions.commonsIO}")
    implementation("javax.xml.bind:jaxb-api:${UserServiceVersions.jaxb}")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:${UserServiceVersions.springCloudAws}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
