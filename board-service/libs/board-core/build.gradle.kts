import BoardServiceVersions.querydsl

plugins {
    kotlin("plugin.noarg") version BoardServiceVersions.kotlin
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20220608.1")

    // querydsl
    implementation("com.querydsl:querydsl-jpa:$querydsl:jakarta")
    kapt("com.querydsl:querydsl-apt:$querydsl:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")
}

noArg {
    annotation("jakarta.persistence.Entity")
}
