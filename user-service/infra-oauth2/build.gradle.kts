dependencies {
    implementation(project(":common-library"))
    implementation(project(":user-service:user-core"))

    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.apache.tomcat.embed:tomcat-embed-core")
}
