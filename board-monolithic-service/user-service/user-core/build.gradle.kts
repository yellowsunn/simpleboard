dependencies {
    implementation(project(":board-monolithic-service:common-library"))
    implementation("org.springframework.data:spring-data-jpa")
    implementation("org.springframework.data:spring-data-commons")

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.persistence:jakarta.persistence-api")

    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.0")
    // TODO: 삭제하기
    implementation("org.hibernate:hibernate-validator:8.0.0.Final")
}
