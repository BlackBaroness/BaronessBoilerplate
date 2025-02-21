plugins {
    `kotlin-conventions`
    `publish-conventions`
    kotlin("plugin.serialization") version "2.1.10"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
}

dependencies {
    api(project(":boilerplate-base"))
    api(project(":boilerplate-paper"))
    compileOnly(libs.adventure.core)
    compileOnly(libs.paper.v1.v16.v5)
    compileOnly(libs.adventure.serializer.gson)
    compileOnly(libs.adventure.serializer.legacy)
    compileOnly(libs.adventure.serializer.plain)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.durationserializer)
    compileOnly(libs.kotlinx.serialization.json)
    compileOnly(libs.placeholerapi)
}
