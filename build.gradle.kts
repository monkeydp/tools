plugins {
    // kotlin
    val kotlinVersion = "1.3.72"
    kotlin("jvm") version kotlinVersion
}

group = "com.monkeydp"
version = "1.1.2-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

dependencies {
    // kotlin
    api(kotlin("reflect"))
    api(kotlin("stdlib-jdk8"))
    // other tool
    implementation("org.apache.commons:commons-lang3:3.8.1")
    // yaml
    implementation("org.yaml:snakeyaml:1.23")
    // json
    api("com.fasterxml.jackson.core:jackson-databind:2.10.4")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.4")
    // log
    api("org.slf4j:jul-to-slf4j:1.7.30")
    api("ch.qos.logback:logback-classic:1.2.3")
    api("org.apache.logging.log4j:log4j-to-slf4j:2.12.1")
    // reflection
    api("org.reflections:reflections:0.9.11")
    // di
    api("org.kodein.di:kodein-di-generic-jvm:6.4.1")
    // command line
    implementation("org.apache.commons:commons-exec:1.3")
    // jakarta
    api("jakarta.validation:jakarta.validation-api:2.0.2")
    // swagger
    api("io.swagger:swagger-annotations:1.5.22")
    // faker
    api("com.github.javafaker:javafaker:1.0.2")
    // test
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.0.0")
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
