plugins {
    kotlin("jvm") version "2.1.0"
    `maven-publish`

}

group = "net.ultragrav"
version = "1.0.15-minestom-4"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("net.minestom:minestom-snapshots:32735340d7")
    compileOnly("net.kyori:adventure-api")
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
    jvmToolchain(21)
}
