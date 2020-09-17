
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-cio:1.4.0")
    implementation(project(":serialization"))
    implementation(project(":database"))
    implementation("io.github.cdimascio:java-dotenv:5.2.2")

    testImplementation("io.ktor:ktor-client-cio:1.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("io.ktor:ktor-server-test-host:1.4.0")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
}
