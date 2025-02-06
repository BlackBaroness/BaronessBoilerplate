plugins {
    `kotlin-conventions`
    `java-library`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
}

dependencies {
    api(project(":module-base"))
    compileOnly(libs.paper.v1.v16.v5)
    compileOnly(libs.adventure.serializer.bungeecord)
}