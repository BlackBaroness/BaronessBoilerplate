package io.github.blackbaroness.boilerplate.configurate.type

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class PostgresConfiguration(
    val address: String = "localhost",
    val port: Int = 5432,
    val database: String = "mydatabase",
    val user: String = "user",
    val password: String = "password",
    val parameters: List<String> = listOf()
)
