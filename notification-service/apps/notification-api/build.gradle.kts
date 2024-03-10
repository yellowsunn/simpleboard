import com.google.cloud.tools.jib.gradle.JibExtension

plugins {
    id("com.google.cloud.tools.jib") version NotificationVersions.jib
    id("org.asciidoctor.jvm.convert") version NotificationVersions.asciidoctorConvert
}
val asciidoctorExtensions: Configuration by configurations.creating

extra["springCloudVersion"] = "2022.0.4"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.data:spring-data-commons")

    implementation(project(":common-library"))
    implementation(project(":notification-service:libs:notification-core"))

    runtimeOnly(project(":notification-service:libs:infra-mongodb"))

    testImplementation("io.mockk:mockk:${NotificationVersions.mockk}")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
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
