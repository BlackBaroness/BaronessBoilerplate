plugins {
    `kotlin-conventions`
    `publish-conventions`
    alias(libs.plugins.kotlin.serialization)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
    maven("https://repo.codemc.io/repository/maven-public/") // NBTAPI
}

dependencies {
    api(project(":boilerplate-base"))
    compileOnly(libs.configurate.core)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.durationserializer)

    // Optional:
    compileOnly(project(":boilerplate-adventure"))
    compileOnly(libs.adventure.core)
    compileOnly(libs.adventure.minimessage)

    // Optional:
    compileOnly(project(":boilerplate-paper"))
    compileOnly(libs.paper.v1.v16.v5)
    compileOnly(libs.adventure.core)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.kotlinx.serialization.json)
    compileOnly(libs.nbtapi)
}
