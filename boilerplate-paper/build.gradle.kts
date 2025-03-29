plugins {
    `kotlin-conventions`
    `publish-conventions`
    kotlin("plugin.serialization") version "2.1.20"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
}

dependencies {
    api(project(":boilerplate-base"))
    api(project(":boilerplate-adventure"))
    compileOnly(libs.paper.v1.v16.v5)
    compileOnly(libs.adventure.serializer.bungeecord)
    compileOnly(libs.adventure.platform.bukkit)
    compileOnly(libs.kotlinx.coroutines)
    compileOnly(libs.mccoroutine.folia)
    compileOnly(libs.placeholderapi)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.caffeine)
    compileOnly(libs.guice.core)
    compileOnly(libs.luckperms)
    compileOnly(libs.kotlinx.serialization.json)
}
