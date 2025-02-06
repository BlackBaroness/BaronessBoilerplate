package io.github.blackbaroness.boilerplates.invui.button

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.google.inject.assistedinject.Assisted
import io.github.blackbaroness.boilerplates.paper.UserExceptionHandler
import io.github.blackbaroness.boilerplates.paper.playSound
import jakarta.inject.Inject
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.plugin.Plugin
import xyz.xenondevs.invui.item.Click
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.impl.AbstractItem

class OneTimeClickButton @Inject constructor(
    private val plugin: Plugin,
    private val userExceptionHandler: UserExceptionHandler,
    @Assisted private val icon: ItemProvider,
    @Assisted private val clickHandler: suspend (Click) -> Unit,
    @Assisted private val sound: Sound? = null
) : AbstractItem() {

    private var clicked = false

    override fun getItemProvider(): ItemProvider = icon

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        if (clicked) return
        clicked = true

        if (sound != null) {
            player.playSound(sound)
        }

        plugin.launch(plugin.entityDispatcher(player)) {
            try {
                clickHandler.invoke(Click(event))
            } catch (e: Throwable) {
                userExceptionHandler.handle(player, e)
            }
        }
    }
}