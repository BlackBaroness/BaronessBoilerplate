package io.github.blackbaroness.boilerplate.invui.configurate

import com.destroystokyo.paper.profile.ProfileProperty
import com.google.common.collect.Multimap
import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.configurate.adventure.type.MiniMessageComponent
import io.github.blackbaroness.boilerplate.configurate.paper.type.AttributeConfiguration
import io.github.blackbaroness.boilerplate.paper.asBukkitColor
import io.github.blackbaroness.boilerplate.paper.asBungeeCordComponents
import io.github.blackbaroness.boilerplate.paper.isNativeAdventureApiAvailable
import io.github.blackbaroness.boilerplate.paper.methodMaterialGetDefaultAttributeModifiers
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import xyz.xenondevs.invui.item.ItemProvider
import java.awt.Color
import java.util.*

@ConfigSerializable
data class ItemTemplate(
    val material: Material,
    val amount: Int? = null,
    val displayName: MiniMessageComponent? = null,
    val lore: List<MiniMessageComponent>? = null,
    val customModelData: Int? = null,
    val flags: Set<ItemFlag>? = null,
    val enchantments: Map<Enchantment, Int>? = null,
    val unbreakable: Boolean? = null,
    val potion: PotionTemplate? = null,
    val headTexture: String? = null,
    val storedEnchantments: Map<Enchantment, Int>? = null,
    val attributes: List<AttributeConfiguration>? = null,
) : ItemProvider {

    @ConfigSerializable
    data class PotionTemplate(
        val type: PotionType,
        val extended: Boolean,
        val upgraded: Boolean,
        val color: Color?,
        val effects: List<PotionEffect>?
    )

    @Suppress("DEPRECATION")
    val unsafeItem by lazy {
        val item = ItemStack(material, amount ?: 1)
        val meta = item.itemMeta ?: throw IllegalStateException("itemMeta is null for '$this'")

        if (displayName != null) {
            if (Boilerplate.isNativeAdventureApiAvailable) {
                meta.displayName(displayName.asComponent())
            } else {
                meta.setDisplayNameComponent(displayName.asBungeeCordComponents)
            }
        }

        if (lore != null) {
            if (Boilerplate.isNativeAdventureApiAvailable) {
                meta.lore(lore.map { it.asComponent() })
            } else {
                meta.loreComponents = lore.map { it.asBungeeCordComponents }
            }
        }

        if (customModelData != null) {
            meta.setCustomModelData(customModelData)
        }

        if (flags != null) {
            Boilerplate.methodMaterialGetDefaultAttributeModifiers?.also { method ->
                @Suppress("UNCHECKED_CAST")
                meta.attributeModifiers = method.invoke(material) as Multimap<Attribute, AttributeModifier>
            }
            meta.addItemFlags(*flags.toTypedArray())
        }

        enchantments?.forEach { (enchantment, level) ->
            meta.addEnchant(enchantment, level, true)
        }

        if (unbreakable != null) {
            meta.isUnbreakable = unbreakable
        }

        if (potion != null && meta is PotionMeta) {
            meta.basePotionData = PotionData(
                potion.type,
                potion.extended,
                potion.extended
            )

            if (potion.color != null) {
                meta.color = potion.color.asBukkitColor
            }

            potion.effects?.forEach { potionEffect ->
                meta.addCustomEffect(potionEffect, true)
            }
        }

        if (headTexture != null && meta is SkullMeta) {
            val uuid = UUID.nameUUIDFromBytes("Skull:$headTexture".encodeToByteArray())
            val profile = Bukkit.createProfile(uuid)
            profile.setProperty(ProfileProperty("textures", headTexture))
            meta.playerProfile = profile
        }

        if (storedEnchantments != null && meta is EnchantmentStorageMeta) {
            storedEnchantments.forEach { (enchantment, level) ->
                meta.addStoredEnchant(enchantment, level, true)
            }
        }

        attributes?.forEach { attribute ->
            meta.addAttributeModifier(attribute.attribute, attribute.modifier)
        }

        item.itemMeta = meta
        return@lazy item
    }

    val safeItem get() = unsafeItem.clone()

    fun resolve(vararg tagResolvers: TagResolver): ItemTemplate {
        return copy(
            displayName = displayName?.resolve(*tagResolvers),
            lore = lore?.map { line -> line.resolve(*tagResolvers) }
        )
    }

    fun resolve(tagResolvers: Collection<TagResolver>) = resolve(*tagResolvers.toTypedArray())

    override fun get(lang: String?): ItemStack = unsafeItem
}

