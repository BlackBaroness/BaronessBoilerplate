plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

rootProject.name = "BaronessBoilerplate"

include(
    "boilerplate-adventure",
    "boilerplate-base",
    "boilerplate-configurate",
    "boilerplate-hibernate",
    "boilerplate-invui",
    "boilerplate-litecommands",
    "boilerplate-paper",
    "boilerplate-bungeecord"
)
