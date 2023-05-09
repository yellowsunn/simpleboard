dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(project(":user-service:user-core"))
    runtimeOnly(project(":user-service:infra-persistence"))
    runtimeOnly(project(":user-service:infra-aws-s3"))
}
