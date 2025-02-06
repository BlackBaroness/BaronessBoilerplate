plugins {
    `kotlin-conventions`
    `java-library`
}

dependencies {
    api(project(":module-base"))
    api(project(":module-configurate"))
    api(project(":module-adventure"))
    compileOnly(libs.configurate.core)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.adventure.core)
    compileOnly(libs.adventure.minimessage)
}