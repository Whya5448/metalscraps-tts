plugins {
    `java-library`
    `maven-publish`

    kotlin("jvm")

    id("org.javamodularity.moduleplugin")
}

val feignVersion = "11.9.1"
val slf4jVersion = "1.7.36"
val log4jVersion = "2.19.0"
val jacksonVersion = "2.13.3"
val jacksonModuleKotlinVersion = "2.13.3"

dependencies {
    // tts-core
    implementation(project(":tts-core"))

    // jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
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