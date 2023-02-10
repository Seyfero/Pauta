import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

extra["springBootVersion"] = "2.7.8"
extra["reactorExtensions"] = "1.1.6"

plugins {
	id("org.springframework.boot") version "2.7.8"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.7.0"
	kotlin("plugin.spring") version "1.7.0"
}

buildscript {
	repositories {
		mavenCentral()
	}

	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
		classpath("org.jetbrains.kotlin:kotlin-allopen:1.7.0")
		classpath("org.springframework.boot:spring-boot-gradle-plugin:2.7.0")
	}
}


group = "com.pauta.administracao"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

subprojects {
	repositories {
		mavenCentral()
	}

	apply {
		plugin("kotlin")
		plugin("kotlin-allopen")
		plugin("io.spring.dependency-management")
		plugin("org.jetbrains.kotlin.jvm")
	}

	allOpen {
		annotation("org.springframework.stereotype.Component")
		annotation("org.springframework.stereotype.Service")
		annotation("org.springframework.stereotype.Repository")
		annotation("org.springframework.context.annotation.ComponentScan")
		annotation("org.springframework.context.annotation.Configuration")
	}

	dependencies {
		// BANCO
//		implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

		// KOTLIN
		implementation("org.springframework.boot:spring-boot-starter-webflux:${property("springBootVersion")}")
		implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:${property("reactorExtensions")}")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.2")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

		testImplementation("org.springframework.boot:spring-boot-starter-test:${property("springBootVersion")}")
		testImplementation("io.projectreactor:reactor-test:3.4.18")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

