package io.github.blackbaroness.boilerplates.invui

import io.github.blackbaroness.boilerplates.invui.button.OneTimeClickButton
import org.bukkit.Sound
import xyz.xenondevs.invui.item.Click
import xyz.xenondevs.invui.item.ItemProvider

interface BoilerplateInvUiFactory {

    fun oneTimeClickButton(
        icon: ItemProvider,
        clickHandler: suspend (Click) -> Unit,
        sound: Sound? = null
    ): OneTimeClickButton

}