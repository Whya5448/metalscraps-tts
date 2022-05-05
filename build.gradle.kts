import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.6.20"
}

group = "org.metalscraps.tts"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}


java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
}