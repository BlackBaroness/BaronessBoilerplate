package io.github.blackbaroness.boilerplate.configurate.serializer

import io.github.blackbaroness.boilerplate.base.asMinecraftTicks
import net.kyori.adventure.util.Ticks
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import java.time.Duration

class PotionEffectSerializer : TypeSerializer<PotionEffect> {

    override fun deserialize(type: Type, node: ConfigurationNode): PotionEffect? {
        val potionEffectType = node.node("type").get(PotionEffectType::class) ?: return null
        val duration = node.node("duration").get(Duration::class) ?: return null
        val amplifier = node.node("amplifier").getInt(1)
        val ambient = node.node("ambient").getBoolean(true)
        val particles = node.node("particles").getBoolean(true)
        val icon = node.node("icon").getBoolean(true)

        return PotionEffect(
            potionEffectType,
            duration.asMinecraftTicks.toInt(),
            amplifier,
            ambient,
            particles,
            icon
        )
    }

    override fun serialize(type: Type, obj: PotionEffect?, node: ConfigurationNode) {
        if (obj == null) {
            node.raw(null)
            return
        }

        node.node("type").set(obj.type)
        node.node("duration").set(Ticks.duration(obj.duration.toLong()))
        node.node("amplifier").set(obj.amplifier)
        node.node("ambient").set(obj.isAmbient)
        node.node("particles").set(obj.hasParticles())
        node.node("icon").set(obj.hasIcon())
    }
}
