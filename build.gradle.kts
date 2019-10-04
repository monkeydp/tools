import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.71"
    kotlin("plugin.spring") version "1.2.71"
}

group = "com.monkeydp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
    // kotlin
    implementation(Deps.kotlinReflect)
    implementation(Deps.kotlinStdlibJdk8)
    // commons-lang3
    implementation(Deps.commonsLang3)
    // lombok
    compileOnly(Deps.lombok)
    annotationProcessor(Deps.lombok)
    // junit
    testImplementation(Deps.junit)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
