plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.yellowsunn"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.apache.commons:commons-lang3")
    implementation("org.apache.commons:commons-collections4:4.4")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        implementation("org.apache.commons:commons-lang3")
        implementation("org.apache.commons:commons-collections4:4.4")

        annotationProcessor("org.projectlombok:lombok")
        compileOnly("org.projectlombok:lombok")
    }
}
