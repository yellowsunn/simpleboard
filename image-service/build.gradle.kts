import com.google.cloud.tools.jib.gradle.JibExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version ImageServiceVersions.springBoot
    id("io.spring.dependency-management") version ImageServiceVersions.springDependencyManagement
    id("com.google.cloud.tools.jib") version ImageServiceVersions.jib
    id("org.asciidoctor.jvm.convert") version "4.0.0-alpha.1"
    kotlin("jvm") version ImageServiceVersions.kotlin
    kotlin("plugin.spring") version ImageServiceVersions.kotlin
}
val asciidoctorExtensions: Configuration by configurations.creating

group = "com.example"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":common-library"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("commons-io:commons-io:${ImageServiceVersions.commonsIO}")
    implementation("javax.xml.bind:jaxb-api:${ImageServiceVersions.jaxb}")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:${ImageServiceVersions.springCloudAws}")
    implementation("org.springframework.restdocs:spring-restdocs-asciidoctor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("io.mockk:mockk:${ImageServiceVersions.mockk}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks {
    val snippetsDir by extra { file("build/generated-snippets") }

    test {
        outputs.dir(snippetsDir)
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        configurations(asciidoctorExtensions.name)
        dependsOn(test)
    }
}

configure<JibExtension> {
    from {
        image = "eclipse-temurin:17-jre-alpine"
    }
    container {
        ports = listOf("8080")
    }
}
