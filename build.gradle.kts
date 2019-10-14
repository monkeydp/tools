import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // kotlin
    kotlin("jvm") version "1.2.71"
}

group = "com.monkeydp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // commons-lang3
    implementation("org.apache.commons:commons-lang3:3.8.1")
    // lombok
    compileOnly("org.projectlombok:lombok:1.18.10")
    annotationProcessor("org.projectlombok:lombok:1.18.10")
    // junit
    testImplementation("junit:junit:4.12")
    // yaml
    implementation("org.yaml:snakeyaml:1.23")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
