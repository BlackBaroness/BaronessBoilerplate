@file:Suppress("UnusedReceiverParameter")

package io.github.blackbaroness.boilerplate.bungeecord

import io.github.blackbaroness.boilerplate.base.Boilerplate
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.md_5.bungee.api.plugin.Plugin

internal var bungeeAudiences: BungeeAudiences? = null

fun Boilerplate.initializeAdventure(plugin: Plugin) {
    if (bungeeAudiences != null) {
        // Already initialized
        return
    }

    bungeeAudiences = BungeeAudiences.create(plugin)
}

fun Boilerplate.destroyAdventure() {
    bungeeAudiences?.close()
}

