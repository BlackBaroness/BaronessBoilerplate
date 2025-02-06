package io.github.blackbaroness.boilerplates.base

interface Service {
    suspend fun setup() {}
    suspend fun reload() {}
    suspend fun destroy() {}
}
