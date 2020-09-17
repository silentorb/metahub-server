plugins {
    base
    id("org.jetbrains.kotlin.jvm") version "1.4.0"
}

group = "silentorb.metahub"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("io.ktor:ktor-client-cio:1.4.0")
    implementation("io.ktor:ktor-server-cio:1.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("io.ktor:ktor-server-test-host:1.4.0")
}
