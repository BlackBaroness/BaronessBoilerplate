plugins {
    `kotlin-conventions`
    `publish-conventions`
    kotlin("plugin.serialization") version "2.1.20"
}

dependencies {
    compileOnly(libs.durationserializer)
    compileOnly(libs.guice.core)
    compileOnly(libs.guice.assistedinject)
    compileOnly(libs.vectorz)
    compileOnly(libs.kotlinx.serialization.json)
    compileOnly(libs.rocksdb)
}
