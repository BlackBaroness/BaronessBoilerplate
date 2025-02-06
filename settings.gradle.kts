plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "BaronessBoilerplate"

include(
    "module-adventure",
    "module-base",
    "module-configurate",
    "module-configurate-adventure",
    "module-configurate-paper",
    "module-hibernate",
    "module-invui",
    "module-litecommands",
    "module-paper",
)
