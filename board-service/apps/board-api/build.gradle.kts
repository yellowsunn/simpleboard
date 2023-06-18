import com.google.cloud.tools.jib.gradle.JibExtension

plugins {
    id("com.google.cloud.tools.jib") version BoardServiceVersions.jib
    id("org.asciidoctor.jvm.convert") version BoardServiceVersions.asciidoctorConvert
}
val asciidoctorExtensions: Configuration by configurations.creating

dependencies {
    implementation(project(":common-library"))
    implementation(project(":board-service:libs:board-core"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.apache.commons:commons-text:1.10.0")

    runtimeOnly(project(":board-service:libs:infra-persistence"))
    runtimeOnly(project(":board-service:libs:infra-http"))
    runtimeOnly(project(":board-service:libs:infra-kafka-producer"))
    runtimeOnly(project(":board-service:libs:infra-mongodb"))
    runtimeOnly(project(":board-service:libs:infra-redis"))

    testImplementation("io.mockk:mockk:${BoardServiceVersions.mockk}")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor")
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
