@file:Suppress("unused")

package io.github.blackbaroness.boilerplate.invui.configurate

import io.github.blackbaroness.boilerplate.configurate.asMiniMessageComponent
import io.github.blackbaroness.boilerplate.configurate.type.MiniMessageComponent
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.ItemWrapper
import xyz.xenondevs.invui.window.Window
import xyz.xenondevs.invui.window.type.context.setTitle
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@ConfigSerializable
open class MenuConfig() {

    var title: MiniMessageComponent = "".asMiniMessageComponent
    var structure: List<String> = listOf()
    var customItems: Map<Char, ItemTemplate> = mapOf()
    var templates: Map<Char, ItemTemplate> = mapOf()

    constructor(
        title: MiniMessageComponent,
        structure: List<String>,
        customItems: Map<Char, ItemTemplate> = mapOf(),
        templates: Map<Char, ItemTemplate> = mapOf()
    ) : this() {
        this.title = title
        this.structure = structure
        this.customItems = customItems
        this.templates = templates
    }

    fun copy(
        title: MiniMessageComponent = this.title,
        structure: List<String> = this.structure,
        customItems: Map<Char, ItemTemplate> = this.customItems,
        templates: Map<Char, ItemTemplate> = this.templates
    ): MenuConfig = MenuConfig(title, structure, customItems, templates)
}

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


