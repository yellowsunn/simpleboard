import BoardServiceVersions.embeddedMongo

dependencies {
    implementation(project(":board-service:libs:board-core"))
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:$embeddedMongo")
}
