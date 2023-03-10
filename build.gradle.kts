import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

extra["springBootVersion"] = "2.7.8"
extra["reactorExtensions"] = "1.1.9"

plugins {
    id("org.springframework.boot") version "2.7.8"
    id("jacoco")
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
}

jacoco {
    toolVersion = "0.8.8"
    reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

tasks.test {
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it).apply {
                    exclude("**/config**")
                    exclude("**/configuration**")
                }
            }
        )
    )
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
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

tasks.bootJar {
    enabled = false
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
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    allOpen {
        annotation("org.springframework.stereotype.Component")
        annotation("org.springframework.stereotype.Service")
        annotation("org.springframework.stereotype.Repository")
        annotation("org.springframework.context.annotation.ComponentScan")
        annotation("org.springframework.context.annotation.Configuration")
    }

    dependencies {
        // KOTLIN
        implementation("org.springframework.boot:spring-boot-starter-webflux:${property("springBootVersion")}")
        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:${property("reactorExtensions")}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21")

        implementation("org.springframework.kafka:spring-kafka:2.8.11")

        implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:${property("springBootVersion")}")
        implementation("io.lettuce:lettuce-core:6.1.10.RELEASE")

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.5")

        testImplementation("org.springframework.boot:spring-boot-starter-test:${property("springBootVersion")}")
        testImplementation("io.projectreactor:reactor-test:3.4.26")

        constraints {
            implementation("io.projectreactor.netty:reactor-netty:1.0.27")
            implementation("io.projectreactor.netty:reactor-nett-corey:1.0.27")
        }
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
