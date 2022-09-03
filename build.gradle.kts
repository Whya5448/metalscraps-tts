import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.7.10"
}

group = "dev.whya.tts"
version = "0.0.2-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    artifacts {
        archives(sourcesJar)
        archives(jar)
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/whya5448/tts-core")
            credentials {
                username = "${project.properties["GITHUB_USERNAME"] ?: System.getenv("GITHUB_USERNAME")}"
                password = "${project.properties["GITHUB_TOKEN"] ?: System.getenv("GITHUB_TOKEN")}"
            }
        }
    }

    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
}