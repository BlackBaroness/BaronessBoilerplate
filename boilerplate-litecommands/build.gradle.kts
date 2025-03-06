plugins {
    `kotlin-conventions`
    `publish-conventions`
}

repositories {
    maven("https://repo.panda-lang.org/releases") // LiteCommands
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
}

dependencies {
    api(project(":boilerplate-base"))
    compileOnly(libs.litecommands.bukkit)
    compileOnly(libs.paper.v1.v16.v5)
    compileOnly(libs.guice.core)
    compileOnly(libs.guice.assistedinject)
}
