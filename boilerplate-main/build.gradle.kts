@file:Suppress("VulnerableLibrariesLocal")

plugins {
    `kotlin-conventions`
    `publish-conventions`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
    maven("https://repo.codemc.io/repository/maven-public/") // NBT-API
    maven("https://repo.panda-lang.org/releases") // LiteCommands
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
    maven("https://repo.xenondevs.xyz/releases") // InvUI
    //maven("https://libraries.minecraft.net/") // Brigadier (for BungeeCord)
}

dependencies {
    // Platforms
    compileOnly(libs.paper.v1.v16.v5)
    compileOnly(libs.bungeecord)

    // Coroutines
    compileOnly(libs.kotlinx.coroutines)
    compileOnly(libs.mccoroutine.bungeecord)
    compileOnly(libs.mccoroutine.folia)

    // Serialization
    compileOnly(libs.kotlinx.serialization.json)
    compileOnly(libs.durationserializer)
    compileOnly(libs.ktoml.core)
    compileOnly(libs.kaml)

    // SQL
    compileOnly(platform(libs.hibernate.platform))
    compileOnly("org.hibernate.orm:hibernate-core")
    compileOnly("org.hibernate.orm:hibernate-hikaricp")
    compileOnly(libs.h2)
    compileOnly(libs.mariadb)
    compileOnly(libs.postgresql)

    // NoSQL
    compileOnly(libs.rocksdb)

    // Dependency injection
    compileOnly(libs.guice.core)
    compileOnly(libs.guice.assistedinject)

    // Adventure
    compileOnly(libs.adventure.core)
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.adventure.serializer.bungeecord)
    compileOnly(libs.adventure.serializer.gson)
    compileOnly(libs.adventure.serializer.legacy)
    compileOnly(libs.adventure.serializer.plain)
    compileOnly(libs.adventure.platform.bukkit)
    compileOnly(libs.adventure.platform.bungeecord)

    // Platform plugins
    compileOnly(libs.luckperms)
    compileOnly(libs.nbtapi)
    compileOnly(libs.placeholderapi)

    // InvUI
    compileOnly(libs.invui.core)
    compileOnly(libs.invui.kotlin)

    // LiteCommands
    compileOnly(libs.litecommands.core)
    compileOnly(libs.litecommands.framework)

    // Math
    compileOnly(libs.joml)
    compileOnly(libs.vectorz)

    // Reflection
    compileOnly(libs.bytebuddy)

    // Cache
    compileOnly(libs.caffeine)

    // Compression
    compileOnly(libs.zstd)
}
