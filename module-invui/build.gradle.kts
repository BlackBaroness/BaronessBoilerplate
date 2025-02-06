plugins {
    `kotlin-conventions`
    `java-library`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
    maven("https://repo.xenondevs.xyz/releases") // InvUI
}

dependencies {
    api(project(":module-base"))
    api(project(":module-paper"))
    api(project(":module-configurate-adventure"))
    api(project(":module-configurate-paper"))
    compileOnly(libs.configurate.core)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.paper.v1.v16.v5)
    compileOnly(libs.adventure.core)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.invui.core)
    compileOnly(libs.invui.kotlin)
    compileOnly(libs.guice.core)
    compileOnly(libs.guice.assistedinject)
    compileOnly(libs.mccoroutine.folia)
    compileOnly(libs.kotlinx.coroutines)
}