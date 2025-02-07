plugins {
    `kotlin-conventions`
    `publish-conventions`
}

repositories {
    maven("https://repo.panda-lang.org/releases") // LiteCommands
}

dependencies {
    api(project(":boilerplate-base"))
    compileOnly(libs.litecommands.core)
    compileOnly(libs.litecommands.framework)
}
