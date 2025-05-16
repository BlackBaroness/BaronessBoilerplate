package io.github.blackbaroness.boilerplate.invui

import io.github.blackbaroness.boilerplate.invui.button.OneTimeClickButton
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
