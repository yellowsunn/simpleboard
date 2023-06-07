plugins {
    id("org.asciidoctor.jvm.convert") version "4.0.0-alpha.1"
}
val asciidoctorExtensions: Configuration by configurations.creating

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
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
