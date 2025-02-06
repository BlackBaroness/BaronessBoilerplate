plugins {
    `kotlin-conventions`
    `java-library`
}

dependencies {
    api(project(":module-base"))
    compileOnly(libs.configurate.core)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.durationserializer)
}