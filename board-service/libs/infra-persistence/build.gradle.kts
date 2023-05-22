import BoardServiceVersions.querydsl

dependencies {
    implementation(project(":board-service:libs:board-core"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.querydsl:querydsl-jpa:$querydsl:jakarta")
    implementation("org.flywaydb:flyway-core")

    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
