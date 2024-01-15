plugins {
    kotlin("jvm") version "1.9.0"
    `maven-publish`
}

group = "net.ultragrav"
version = "1.0.8"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.14.0")
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}
