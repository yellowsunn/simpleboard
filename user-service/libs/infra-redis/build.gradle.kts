dependencies {
    implementation(project(":user-service:libs:user-core"))

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    testImplementation("it.ozimov:embedded-redis:0.7.3") {
        exclude("org.slf4j", "slf4j-simple")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
