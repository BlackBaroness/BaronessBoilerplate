plugins {
    `kotlin-conventions`
    `publish-conventions`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
}

dependencies {
    api(project(":boilerplate-base"))
    api(project(":boilerplate-adventure"))
    compileOnly(libs.paper.v1.v16.v5)
    compileOnly(libs.adventure.serializer.bungeecord)
    compileOnly(libs.adventure.platform.bukkit)
}
