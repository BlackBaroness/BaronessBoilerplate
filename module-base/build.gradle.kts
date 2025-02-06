plugins {
    `kotlin-conventions`
    `publish-conventions`
}

dependencies {
    compileOnly(libs.durationserializer)
    compileOnly(libs.guice.core)
    compileOnly(libs.guice.assistedinject)
}
