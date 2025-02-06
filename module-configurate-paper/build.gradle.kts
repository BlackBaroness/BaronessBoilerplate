plugins {
    `kotlin-conventions`
    `java-library`
    kotlin("plugin.serialization") version "2.1.10"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
    maven("https://repo.codemc.io/repository/maven-public/") // NBTAPI
}

dependencies {
    api(project(":module-base"))
    api(project(":module-paper"))
    api(project(":module-configurate-adventure"))
    compileOnly(libs.configurate.core)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.paper.v1.v16.v5)
    compileOnly(libs.adventure.core)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.kotlinx.serialization.json)
    compileOnly(libs.nbtapi)
}