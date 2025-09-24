plugins {
    java
    id("org.springframework.boot") version "3.2.7"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.neptunesoftware"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val libs = file("libs")
    if (libs.exists()) {
        implementation(fileTree(mapOf("dir" to libs, "include" to listOf("*.jar"))))
    }

   // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")

   // Database
    runtimeOnly("com.oracle.database.jdbc:ojdbc10:19.20.0.0")

   // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

   // PDF Generation
    implementation("com.itextpdf:itextpdf:5.5.13.3")

    // Utilities
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot:3.0.4")

    // JSON Processing
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Complete JAX-WS stack
    implementation("com.sun.xml.ws:jaxws-ri:2.3.5") {
        exclude(group = "com.oracle.weblogic", module = "*")
    }
    implementation("com.sun.xml.messaging.saaj:saaj-impl:1.5.3")
    implementation("javax.xml.soap:javax.xml.soap-api:1.4.0")
    implementation("javax.xml.ws:jaxws-api:2.3.1")
    implementation("com.sun.xml.ws:policy:2.7.10")

// JAX-B dependencies
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.sun.xml.bind:jaxb-core:2.3.0.1")
    implementation("com.sun.xml.bind:jaxb-impl:2.3.5")

// Activation
    implementation("javax.activation:activation:1.1.1")


    implementation("javax.xml.ws:jaxws-api:2.3.1")

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    implementation("xerces:xercesImpl:2.12.2")
    implementation("xalan:xalan:2.7.3")

    implementation("com.sun.xml.bind:jaxb-impl:2.3.5")
    implementation("javax.xml.bind:jaxb-api:2.3.1")

    // Servlet
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
