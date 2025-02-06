plugins {
    `kotlin-conventions`
    `java-library`
}

dependencies {
    compileOnly(libs.durationserializer)
    compileOnly(libs.guice.core)
    compileOnly(libs.guice.assistedinject)
}