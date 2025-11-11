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
import xyz.xenondevs.invui.item.impl.controlitem.ControlItem
import kotlin.math.max

abstract class PageButton(
    protected val pagePresentTemplate: ItemTemplate,
    protected val pageAbsentTemplate: ItemTemplate,
    protected val sound: Sound?,
) : ControlItem<PagedGui<*>>() {

    protected abstract val displayedTargetPage: Int

    override fun getItemProvider(gui: PagedGui<*>): ItemProvider {
        val item = if (gui.hasNextPage()) pagePresentTemplate else pageAbsentTemplate
        return item.resolve(
            Boilerplate.tagResolver("current_page", gui.currentPage + 1),
            Boilerplate.tagResolver("page_amount", max(1, gui.pageAmount)),
            Boilerplate.tagResolver("target_page", displayedTargetPage),
        )
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        if (!clickType.isLeftClick) return
        if (!canMove()) return

        move()

        if (sound != null) {
            player.playSound(sound)
        }
    }

    protected abstract fun canMove(): Boolean

    protected abstract fun move()
}
