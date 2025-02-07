plugins {
    `kotlin-conventions`
    `publish-conventions`
}

dependencies {
    api(project(":boilerplate-base"))
    compileOnly(libs.adventure.core)
    compileOnly(libs.adventure.serializer.gson)
    compileOnly(libs.adventure.serializer.legacy)
    compileOnly(libs.adventure.serializer.plain)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.durationserializer)
}
