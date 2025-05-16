@file:Suppress("unused")

package io.github.blackbaroness.boilerplate.kotlinx.serialization.type

import com.akuleshov7.ktoml.annotations.TomlMultiline
import io.github.blackbaroness.boilerplate.kotlinx.serialization.serializer.CharStringSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.ItemWrapper
import xyz.xenondevs.invui.window.Window
import xyz.xenondevs.invui.window.type.context.setTitle
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Serializable
open class MenuConfig(
    val title: @Contextual MiniMessageComponent = "".asMiniMessageComponent,
    @TomlMultiline
    val structure: List<String> = listOf(),
    val customItems: Map<@Serializable(CharStringSerializer::class) Char, ItemTemplate> = mapOf(),
    val templates: Map<@Serializable(CharStringSerializer::class) Char, ItemTemplate> = mapOf()
)

@OptIn(ExperimentalContracts::class)
inline fun Window.Builder.Normal.Single.import(
    config: MenuConfig,
    guiModifier: PagedGui.Builder<Item>.() -> Unit
): PagedGui<Item> {
    contract {
        callsInPlace(guiModifier, InvocationKind.EXACTLY_ONCE)
    }
    setTitle(config.title.asComponent())
    val gui = config.createPagedGui().apply(guiModifier).build()
    setGui(gui)
    return gui
}

fun MenuConfig.createPagedGui(): PagedGui.Builder<Item> = PagedGui.items().apply {
    setStructure(*this@createPagedGui.structure.toTypedArray())
    addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
    customItems.forEach { (key, item) ->
        addIngredient(key, ItemWrapper(item.unsafeItem))
    }
}


