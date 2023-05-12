dependencies {
    implementation(project(":common-library"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("at.favre.lib:bcrypt:0.10.2")

    // querydsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    implementation("commons-codec:commons-codec")
    implementation("com.fasterxml.jackson.core:jackson-databind:${UserServiceVersions.jackson}")
    implementation("io.jsonwebtoken:jjwt-api:${UserServiceVersions.jjwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${UserServiceVersions.jjwt}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${UserServiceVersions.jjwt}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
