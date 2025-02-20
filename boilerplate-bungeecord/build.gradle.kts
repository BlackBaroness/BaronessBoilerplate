plugins {
    `kotlin-conventions`
    `publish-conventions`
}

repositories {
    maven("https://libraries.minecraft.net/") // Brigadier (for BungeeCord)
}

dependencies {
    api(project(":boilerplate-base"))
    api(project(":boilerplate-adventure"))
    compileOnly(libs.bungeecord)
    compileOnly(libs.adventure.serializer.bungeecord)
    compileOnly(libs.adventure.platform.bungeecord)
    compileOnly(libs.kotlinx.coroutines)
    compileOnly(libs.mccoroutine.bungeecord)
    compileOnly(libs.bytebuddy)
}
