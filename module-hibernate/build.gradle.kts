plugins {
    `kotlin-conventions`
    `java-library`
}

dependencies {
    api(project(":module-base"))
    api(project(":module-configurate"))
    compileOnly(platform("org.hibernate.orm:hibernate-platform:6.6.6.Final"))
    compileOnly("org.hibernate.orm:hibernate-core")
    compileOnly("org.hibernate.orm:hibernate-hikaricp")
    compileOnly("com.h2database:h2:2.3.232")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:3.5.1")
}