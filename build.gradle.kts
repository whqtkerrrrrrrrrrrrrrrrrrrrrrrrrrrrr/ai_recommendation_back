plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.ll"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    dependencies {
        // Spring Boot Starter
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-web")
        developmentOnly("org.springframework.boot:spring-boot-devtools")

        // Lombok
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        // DB
        runtimeOnly("com.mysql:mysql-connector-j")

        // Jsoup
        implementation("org.jsoup:jsoup:1.15.3")

        // JWT
        implementation("io.jsonwebtoken:jjwt-api:0.12.6")
        runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
        runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

        // SpringBoot Security
        implementation("org.springframework.boot:spring-boot-starter-security")

        // OAuth2
        implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

        // Test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

}

tasks.withType<Test> {
    useJUnitPlatform()
}
