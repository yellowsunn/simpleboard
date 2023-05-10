dependencies {
    implementation(project(":user-service:user-core"))
    implementation("commons-io:commons-io:${UserServiceVersions.commonsIO}")
    implementation("javax.xml.bind:jaxb-api:${UserServiceVersions.jaxb}")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:${UserServiceVersions.springCloudAws}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
