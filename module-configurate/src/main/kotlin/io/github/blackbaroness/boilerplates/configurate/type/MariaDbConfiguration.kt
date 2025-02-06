package io.github.blackbaroness.boilerplates.configurate.type

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class MariaDbConfiguration(
    val address: String = "localhost",
    val port: Int = 3306,
    val database: String = "mydatabase",
    val user: String = "user",
    val password: String = "password",
    val parameters: List<String> = listOf(),
)
