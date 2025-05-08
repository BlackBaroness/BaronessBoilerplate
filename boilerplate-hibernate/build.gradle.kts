plugins {
    `kotlin-conventions`
    `publish-conventions`
}

dependencies {
    api(project(":boilerplate-base"))
    api(project(":boilerplate-configurate"))
    api(project(":boilerplate-adventure"))

    // Hibernate
    compileOnly(platform(libs.hibernate.platform))
    compileOnly("org.hibernate.orm:hibernate-core")
    compileOnly("org.hibernate.orm:hibernate-hikaricp")

    // Database drivers
    compileOnly(libs.mariadb)
    compileOnly(libs.postgresql)
    compileOnly(libs.h2)

    compileOnly(libs.kotlinx.serialization.json)
    compileOnly(libs.adventure.core)
    compileOnly(libs.adventure.serializer.gson)
}
