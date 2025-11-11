package io.github.blackbaroness.boilerplate.invui.button

import io.github.blackbaroness.boilerplate.kotlinx.serialization.type.ItemTemplate
import net.kyori.adventure.text.Component
import org.bukkit.Sound

class NextPageButton(
    pagePresentTemplate: ItemTemplate,
    pageAbsentTemplate: ItemTemplate,
    sound: Sound? = null,
    windowTitleChanger: ((currentPage: Int, totalPages: Int) -> Component)? = null,
) : PageButton(pagePresentTemplate, pageAbsentTemplate, sound, windowTitleChanger) {

    override val displayedTargetPage: Int
        get() = gui.currentPage + 2

    override fun canMove(): Boolean {
        return gui.hasNextPage()
    }

    override fun move() {
        gui.goForward()
    }

}
