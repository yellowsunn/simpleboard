dependencies {
    implementation(project(":board-monolithic-service:board-service:board-core"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    implementation(project(mapOf("path" to ":board-monolithic-service:user-service:user-core")))
}
