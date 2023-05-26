dependencies {
    implementation(project(":common-library"))
    implementation(project(":user-service:libs:user-core"))
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
