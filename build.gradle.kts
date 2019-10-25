import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // kotlin
    val kotlinVersion = "1.3.50"
    kotlin("jvm") version kotlinVersion
}

group = "com.monkeydp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
    // kotlin
    api("org.jetbrains.kotlin:kotlin-reflect")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // commons-lang3
    implementation("org.apache.commons:commons-lang3:3.8.1")
    // junit
    testImplementation("junit:junit:4.12")
    // snakeyaml
    implementation("org.yaml:snakeyaml:1.23")
    // jackson
    api("com.fasterxml.jackson.core:jackson-databind:2.9.9.3")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
