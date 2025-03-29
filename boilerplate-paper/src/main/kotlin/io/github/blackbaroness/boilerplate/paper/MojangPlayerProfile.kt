package io.github.blackbaroness.boilerplate.paper

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class MojangPlayerProfile @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid,
    val name: String,
    val properties: List<JsonObject>
) {

    @Serializable
    data class Texture(
        val signature: String?,
        val value: String
    )

    @delegate:Transient
    val texture: Texture? by lazy {
        properties.find { it["name"]!!.jsonPrimitive.content == "textures" }?.let {
            Json.decodeFromJsonElement<Texture>(it)
        }
    }
}
