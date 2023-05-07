plugins {
    id("org.springframework.boot") version UserServiceVersions.springBoot
    id("io.spring.dependency-management") version UserServiceVersions.springDependencyManagement
}

group = "com.yellowsunn"
version = "0.0.1-SNAPSHOT"

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
