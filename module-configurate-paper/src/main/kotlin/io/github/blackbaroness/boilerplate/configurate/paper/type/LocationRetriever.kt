package io.github.blackbaroness.boilerplate.configurate.paper.type

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.Location

@Serializable
data class LocationRetriever(
    val worldName: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float?,
    val pitch: Float?,
) {

    val unsafeLocation by lazy {
        Location(
            Bukkit.getWorld(worldName) ?: throw IllegalArgumentException("Unable to find a world '$worldName'"),
            x,
            y,
            z,
            yaw ?: 0f,
            pitch ?: 0f
        )
    }

    val safeLocation get() = unsafeLocation.clone()

    override fun toString() = Json.encodeToString(this)

    companion object {
        fun fromString(string: String) = Json.decodeFromString<io.github.blackbaroness.boilerplate.configurate.paper.type.LocationRetriever>(string)
    }
}
