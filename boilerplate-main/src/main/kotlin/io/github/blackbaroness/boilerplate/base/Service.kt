package io.github.blackbaroness.boilerplate.base

interface Service {
    suspend fun setup() {}
    suspend fun reload() {}
    suspend fun destroy() {}
}
