dependencies {
    implementation(project(":user-service:user-core"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
