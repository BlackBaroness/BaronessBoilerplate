package io.github.blackbaroness.boilerplate.ktoml.type

import kotlinx.serialization.Serializable

@Serializable
data class PostgresConfiguration(
    val address: String = "localhost",
    val port: Int = 5432,
    val database: String = "mydatabase",
    val user: String = "user",
    val password: String = "password",
    val parameters: List<String> = listOf()
)
