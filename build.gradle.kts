plugins {
    kotlin("jvm") version "2.3.0"
    `maven-publish`

}

group = "net.ultragrav"
version = "1.0.16-minestom-1.21.11"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("net.minestom:minestom:2026.06.20-26.1.2")
    compileOnly("net.kyori:adventure-api")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.3")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}
kotlin {
    jvmToolchain(25)
}
