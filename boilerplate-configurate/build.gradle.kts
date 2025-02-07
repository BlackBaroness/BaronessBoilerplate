plugins {
    `kotlin-conventions`
    `publish-conventions`
}

dependencies {
    api(project(":boilerplate-base"))
    compileOnly(libs.configurate.core)
    compileOnly(libs.configurate.extra.kotlin)
    compileOnly(libs.durationserializer)
}
