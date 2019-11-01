import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // kotlin
    val kotlinVersion = "1.3.50"
    kotlin("jvm") version kotlinVersion
}

group = "com.monkeydp"
version = "0.0.2-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
    // kotlin
    api("org.jetbrains.kotlin:kotlin-reflect")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // groovy
    implementation("org.codehaus.groovy:groovy-all:2.5.8")
    // tool
    implementation("org.apache.commons:commons-lang3:3.8.1")
    // yaml
    implementation("org.yaml:snakeyaml:1.23")
    // json
    api("com.fasterxml.jackson.core:jackson-databind:2.9.9.3")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")
    // log
    api("org.slf4j:slf4j-api:1.7.28")
    api("ch.qos.logback:logback-classic:1.2.3")
    api("org.slf4j:jul-to-slf4j:1.7.28")
    // reflection
    api("org.reflections:reflections:0.9.11")
    // test
    testImplementation("junit:junit:4.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
