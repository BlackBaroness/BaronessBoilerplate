package io.github.blackbaroness.boilerplate.invui.button

import io.github.blackbaroness.boilerplate.adventure.tagResolver
import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.kotlinx.serialization.type.ItemTemplate
import io.github.blackbaroness.boilerplate.paper.playSound
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.impl.controlitem.PageItem
import kotlin.math.max

class PreviousPageButton(
    private val pagePresentTemplate: ItemTemplate,
    private val pageAbsentTemplate: ItemTemplate,
    private val sound: Sound? = null,
) : PageItem(false) {

    override fun getItemProvider(gui: PagedGui<*>): ItemProvider {
        val item = if (gui.hasPreviousPage()) pagePresentTemplate else pageAbsentTemplate
        return item.resolve(
            Boilerplate.tagResolver("current_page", gui.currentPage + 1),
            Boilerplate.tagResolver("page_amount", max(1, gui.pageAmount)),
            Boilerplate.tagResolver("target_page", gui.currentPage),
        )
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        super.handleClick(clickType, player, event)
        if (sound != null) {
            player.playSound(sound)
        }
    }
}
