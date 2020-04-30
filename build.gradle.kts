plugins {
    // kotlin
    val kotlinVersion = "1.3.50"
    kotlin("jvm") version kotlinVersion
}

group = "com.monkeydp"
version = "0.0.4-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
    // kotlin
    api(kotlin("reflect"))
    api(kotlin("stdlib-jdk8"))
    // other tool
    implementation("org.apache.commons:commons-lang3:3.8.1")
    // yaml
    implementation("org.yaml:snakeyaml:1.23")
    // json
    api("com.fasterxml.jackson.core:jackson-databind:2.9.9.3")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")
    // log
    api("org.springframework.boot:spring-boot-starter-logging:2.1.9.RELEASE")
    // reflection
    api("org.reflections:reflections:0.9.11")
    // di
    api("org.kodein.di:kodein-di-generic-jvm:6.4.1")
    // command line
    implementation("org.apache.commons:commons-exec:1.3")
    // test
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
    test {
        useJUnitPlatform()
    }
}