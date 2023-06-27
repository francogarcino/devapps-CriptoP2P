import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.jetbrains.kotlin.jvm") version "1.7.22"
	id("org.jetbrains.kotlin.plugin.spring") version "1.7.22"
	id("org.jetbrains.kotlin.plugin.jpa") version "1.7.22"
	id("org.sonarqube") version "3.5.0.2730"
	id("jacoco")
}

group = "ar.edu.unq.desapp.grupog"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("javax.cache:cache-api:1.1.1")
	implementation("org.ehcache:ehcache:3.8.1")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.20")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.tngtech.archunit:archunit:1.0.1")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")
}

sonarqube {
	properties {
		property("sonar.projectKey", "francogarcino_devapps-CriptoP2P")
		property("sonar.organization", "francogarcino")
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.projectName", "devapps-CriptoP2P")
		property("sonar.login", "2a3fabd2618e5d914324b42a00da48b7d55c0897")
		property("sonar.kotlin.compiler.version", "1.7.22")
		property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir\\reports\\jacoco\\test\\jacocoTestReport.xml")
	}
}

jacoco {
	toolVersion = "0.8.8"
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
		csv.required.set(false)
		html.required.set(false)
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<Jar>("jar") {
	enabled = false
}
