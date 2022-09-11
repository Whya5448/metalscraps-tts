import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream

plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.7.10" apply false

    id("org.javamodularity.moduleplugin") version "1.8.12" apply false
}

allprojects {
    group = "dev.whya.tts"
    version = "0.0.2-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply {
        apply(plugin = "java")
        plugin("org.gradle.java-library")
        plugin("org.gradle.maven-publish")
        plugin("org.jetbrains.kotlin.jvm")
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

    tasks.withType<Jar> {
        archiveClassifier.set("")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        withSourcesJar()
    }

    tasks {
/*        val sourcesJar by creating(Jar::class) {
            archiveClassifier.set("sources")
            from(sourceSets.main.get().allSource)
        }
*/
/*
        artifacts {
            archives(sourcesJar)
            archives(jar)
        }
        */
    }

    fun getGitHash(): String {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine("git", "rev-parse", "--short", "HEAD")
            standardOutput = stdout
        }
        return stdout.toString().trim()
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
            create<MavenPublication>("library") {
                from(components["java"])
            }

            create<MavenPublication>("snapshot") {
                version = getGitHash()
                from(components["java"])
            }
        }
    }
}