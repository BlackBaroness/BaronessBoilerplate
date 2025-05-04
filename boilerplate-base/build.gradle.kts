plugins {
    `kotlin-conventions`
    `publish-conventions`
}

dependencies {
    compileOnly(libs.durationserializer)
    compileOnly(libs.guice.core)
    compileOnly(libs.guice.assistedinject)
    compileOnly(libs.vectorz)
    compileOnly(libs.kotlinx.serialization.json)
    compileOnly(libs.rocksdb)
    compileOnly(libs.zstd)
    compileOnly(libs.joml)
}
