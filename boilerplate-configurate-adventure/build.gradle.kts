plugins {
    `kotlin-conventions`
    `publish-conventions`
}

dependencies {
    api(project(":boilerplate-base"))
    api(project(":boilerplate-configurate"))
    api(project(":boilerplate-adventure"))
    compileOnly(libs.configurate.core)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.adventure.core)
    compileOnly(libs.adventure.minimessage)
}
