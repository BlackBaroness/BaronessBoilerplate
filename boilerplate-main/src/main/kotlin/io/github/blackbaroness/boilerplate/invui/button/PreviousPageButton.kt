package io.github.blackbaroness.boilerplate.invui.button

import io.github.blackbaroness.boilerplate.adventure.tagResolver
import io.github.blackbaroness.boilerplate.base.Boilerplate
import io.github.blackbaroness.boilerplate.kotlinx.serialization.type.ItemTemplate
import io.github.blackbaroness.boilerplate.paper.playSound
import net.kyori.adventure.text.Component
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.impl.controlitem.ControlItem
import xyz.xenondevs.invui.window.changeTitle
import kotlin.math.max

class PreviousPageButton(
    private val pagePresentTemplate: ItemTemplate,
    private val pageAbsentTemplate: ItemTemplate,
    private val sound: Sound? = null,
    private val windowTitleChanger: ((currentPage: Int, targetPage: Int) -> Component)? = null,
) : ControlItem<PagedGui<*>>() {

    override fun getItemProvider(gui: PagedGui<*>): ItemProvider {
        val item = if (gui.hasPreviousPage()) pagePresentTemplate else pageAbsentTemplate
        return item.resolve(
            Boilerplate.tagResolver("current_page", gui.currentPage + 1),
            Boilerplate.tagResolver("page_amount", max(1, gui.pageAmount)),
            Boilerplate.tagResolver("target_page", gui.currentPage),
        )
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        if (!clickType.isLeftClick) return
        if (!gui.hasPreviousPage()) return

        gui.goBack()

        if (windowTitleChanger != null) {
            val title = windowTitleChanger.invoke(gui.currentPage + 1, gui.currentPage)
            for (window in windows) {
                window.changeTitle(title.asComponent())
            }
        }

        if (sound != null) {
            player.playSound(sound)
        }
    }
}
