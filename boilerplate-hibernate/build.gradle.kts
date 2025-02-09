plugins {
    `kotlin-conventions`
    `publish-conventions`
}

dependencies {
    api(project(":boilerplate-base"))
    api(project(":boilerplate-configurate"))

    // Hibernate
    compileOnly(platform(libs.hibernate.platform))
    compileOnly("org.hibernate.orm:hibernate-core")
    compileOnly("org.hibernate.orm:hibernate-hikaricp")

    // Database drivers
    compileOnly(libs.mariadb)
    compileOnly(libs.postgresql)
    compileOnly(libs.h2)
}
