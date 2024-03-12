import com.google.cloud.tools.jib.gradle.JibExtension

plugins {
    id("com.google.cloud.tools.jib") version UserServiceVersions.jib
    id("org.asciidoctor.jvm.convert") version "4.0.0-alpha.1"
}
val asciidoctorExtensions: Configuration by configurations.creating

extra["springCloudVersion"] = "2022.0.4"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.session:spring-session-data-redis")

    implementation(project(":common-library"))
    implementation(project(":user-service:libs:user-core"))
    runtimeOnly(project(":user-service:libs:infra-persistence"))
    runtimeOnly(project(":user-service:libs:infra-redis"))
    runtimeOnly(project(":user-service:libs:infra-http"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
        image = "yellowsunn/eclipse-temurin:17-jre-alpine-pinpoint-2.5.2"
    }
    container {
        ports = listOf("8080")
    }
}
