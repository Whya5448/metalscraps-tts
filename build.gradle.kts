plugins {
    `java-library`
    `maven-publish`

    kotlin("jvm") version "1.7.20"
    kotlin("plugin.spring") version "1.7.20"
    kotlin("plugin.jpa") version "1.7.20"

    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

extra["springCloudVersion"] = "2021.0.4"

group = "dev.whya"
version = "0.0.2"

repositories {
    mavenCentral()
    maven("https://maven.pkg.github.com/whya5448/metalscraps-tts")
}

apply(plugin = "java")
apply(plugin = "io.spring.dependency-management")

apply(plugin = "org.gradle.java-library")
apply(plugin = "org.gradle.maven-publish")
apply(plugin = "org.jetbrains.kotlin.jvm")

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
    generatedPomCustomization {
        enabled(false)
    }
}

val feignVersion = "12.0"
val slf4jVersion = "2.0.3"
val log4jVersion = "2.19.0"
val jacksonModuleKotlinVersion = "2.13.4"

dependencies {
    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleKotlinVersion")

    // feign
    implementation("io.github.openfeign:feign-core:$feignVersion")
    implementation("io.github.openfeign:feign-slf4j:$feignVersion")
    implementation("io.github.openfeign:feign-jackson:$feignVersion")
    implementation("io.github.openfeign:feign-jaxb:$feignVersion")

    // jaxb
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.4.0-b180830.0438")

    // logging
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    testImplementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    archiveClassifier.set("")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/whya5448/metalscraps-tts")
            credentials {
                username = "${project.properties["GITHUB_USERNAME"] ?: System.getenv("GITHUB_USERNAME")}"
                password = "${project.properties["GITHUB_TOKEN"] ?: System.getenv("GITHUB_TOKEN")}"
            }
        }
    }

    publications {
        register<MavenPublication>("library") {
            from(components["java"])
        }
        register<MavenPublication>("snapshot") {
            version = "$version-SNAPSHOT"
            from(components["java"])
        }
    }
}
