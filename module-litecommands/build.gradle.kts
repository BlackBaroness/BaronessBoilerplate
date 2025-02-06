plugins {
    `kotlin-conventions`
    `java-library`
}

repositories {
    maven("https://repo.panda-lang.org/releases") // LiteCommands
}

dependencies {
    api(project(":module-base"))
    compileOnly(libs.litecommands.core)
    compileOnly(libs.litecommands.framework)
}
