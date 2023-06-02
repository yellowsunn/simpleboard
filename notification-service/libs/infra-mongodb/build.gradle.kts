import BoardServiceVersions.embeddedMongo

dependencies {
    implementation(project(":common-library"))

    implementation(project(":notification-service:libs:notification-core"))
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:$embeddedMongo")
}
