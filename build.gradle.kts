plugins {
    java
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    java.sourceCompatibility = JavaVersion.VERSION_17

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
