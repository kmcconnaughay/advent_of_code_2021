version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.6.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.6.0")
    implementation("org.typemeta:funcj-parser:0.6.16")
    implementation("org.jetbrains.kotlinx:multik-api:0.1.1")
    implementation("org.jetbrains.kotlinx:multik-default:0.1.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava {
    enabled = false
}

tasks.compileTestJava {
    enabled = false
}

application {
    mainClass.set("AdventOfCode2021Kt")
}
