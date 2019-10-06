import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.71"
}

group = "com.monkeydp.common"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
    // kotlin
    implementation(Libs.kotlinReflect)
    implementation(Libs.kotlinStdlibJdk8)
    // commons-lang3
    implementation(Libs.commonsLang3)
    // lombok
    compileOnly(Libs.lombok)
    annotationProcessor(Libs.lombok)
    // junit
    testImplementation(Libs.junit)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
