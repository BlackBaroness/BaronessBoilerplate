package io.github.blackbaroness.boilerplate.litecommands

import dev.rollczi.litecommands.LiteCommandsBuilder
import dev.rollczi.litecommands.argument.ArgumentKey
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBase
import dev.rollczi.litecommands.handler.result.ResultHandlerChain
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.platform.PlatformSettings

inline fun <SENDER, SETTINGS : PlatformSettings, BUILDER : LiteCommandsBuilder<SENDER, SETTINGS, BUILDER>, reified ARG> BUILDER.argument(
    resolver: ArgumentResolverBase<SENDER, ARG>,
    key: ArgumentKey? = null,
): BUILDER {
    return if (key == null) {
        argument(ARG::class.java, resolver)
    } else {
        argument(ARG::class.java, key, resolver)
    }
}

inline fun <SENDER, SETTINGS : PlatformSettings, BUILDER : LiteCommandsBuilder<SENDER, SETTINGS, BUILDER>, reified TYPE> BUILDER.result(
    crossinline handler: (Invocation<SENDER>, TYPE, ResultHandlerChain<SENDER>) -> Unit,
): BUILDER = result(TYPE::class.java) { invocation, type, chain -> handler.invoke(invocation, type, chain) }
