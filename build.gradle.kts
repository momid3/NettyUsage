plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com.momid"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.netty:netty-all:4.1.65.Final")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(16)
}
