
dependencies {
    api("com.fasterxml.jackson.core:jackson-databind:2.11.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.11.2")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-reflect", version = "1.4.0")
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-afterburner", version = "2.11.2")
}
