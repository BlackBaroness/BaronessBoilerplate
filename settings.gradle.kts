plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "BaronessBoilerplate"

include(
    "boilerplate-adventure",
    "boilerplate-base",
    "boilerplate-configurate",
    "boilerplate-configurate-adventure",
    "boilerplate-configurate-paper",
    "boilerplate-hibernate",
    "boilerplate-invui",
    "boilerplate-litecommands",
    "boilerplate-paper",
)
