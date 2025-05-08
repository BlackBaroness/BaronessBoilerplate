package io.github.blackbaroness.boilerplate.invui

import io.github.blackbaroness.boilerplate.configurate.type.MiniMessageComponent
import org.jetbrains.annotations.Contract
import xyz.xenondevs.invui.window.Window
import xyz.xenondevs.invui.window.type.context.setTitle

@Contract("_ -> this")
fun <B : Window.Builder<*, B>> B.setTitle(title: MiniMessageComponent): B = setTitle(title.asComponent())
