plugins {
	java
	id("org.springframework.boot") version "3.4.8"
	id("io.spring.dependency-management") version "1.1.7"
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

	implementation("javax.xml.ws:jaxws-api:2.3.1")
	implementation("com.itextpdf:itextpdf:5.5.13.3")
	implementation("org.projectlombok:lombok:1.18.38")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.github.ulisesbocchio:jasypt-spring-boot:3.0.4")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	runtimeOnly("com.oracle.database.jdbc:ojdbc11")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
