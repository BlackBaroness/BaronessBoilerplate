plugins {
    `kotlin-conventions`
    `publish-conventions`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
    maven("https://repo.xenondevs.xyz/releases") // InvUI
}

dependencies {
    api(project(":boilerplate-base"))
    api(project(":boilerplate-paper"))
    api(project(":boilerplate-configurate"))
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
