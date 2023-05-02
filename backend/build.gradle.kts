import com.ewerk.gradle.plugins.QuerydslPluginExtension
import com.ewerk.gradle.plugins.tasks.QuerydslCompile

plugins {
    java
    id("org.springframework.boot") version "2.5.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
}

group = "com.yellowsunn"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    named("querydsl") {
        extendsFrom(configurations.compileClasspath.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.navercorp.lucy:lucy-xss-servlet:2.0.1")
    implementation("com.querydsl:querydsl-jpa")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val querydslDir = "$buildDir/generated/querydsl"

configure<QuerydslPluginExtension> {
    jpa = true
    querydslSourcesDir = querydslDir
}

sourceSets.getByName("main") {
    java.srcDir(querydslDir)
}

tasks.withType<QuerydslCompile> {
    options.annotationProcessorPath = configurations.querydsl.get()
}
