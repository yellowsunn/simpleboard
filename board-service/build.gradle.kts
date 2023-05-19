import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version BoardServiceVersions.springBoot
    id("io.spring.dependency-management") version BoardServiceVersions.springDependencyManagement
    kotlin("jvm") version BoardServiceVersions.kotlin
    kotlin("plugin.spring") version BoardServiceVersions.kotlin
    kotlin("plugin.noarg") version BoardServiceVersions.kotlin
    kotlin("kapt") version BoardServiceVersions.kotlin
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

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}
