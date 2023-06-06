import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version NotificationVersions.springBoot
    id("io.spring.dependency-management") version NotificationVersions.springDependencyManagement
    kotlin("jvm") version NotificationVersions.kotlin
    kotlin("plugin.spring") version NotificationVersions.kotlin
    kotlin("plugin.noarg") version NotificationVersions.kotlin
    kotlin("kapt") version NotificationVersions.kotlin
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "kotlin-kapt")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
}
