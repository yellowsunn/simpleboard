dependencies {
    implementation(project(":board-service:libs:board-core"))

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    testImplementation("it.ozimov:embedded-redis:0.7.3") {
        exclude("org.slf4j", "slf4j-simple")
    }
}
