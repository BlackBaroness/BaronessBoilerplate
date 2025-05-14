package io.github.blackbaroness.boilerplate.ktoml.type

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import java.time.Duration

@Serializable
data class TitleConfiguration(
    @Contextual val title: MiniMessageComponent,
    @Contextual val subtitle: MiniMessageComponent,
    @Contextual val durationFadeIn: Duration = Ticks.duration(10),
    @Contextual val durationStay: Duration = Ticks.duration(70),
    @Contextual val durationFadeOut: Duration = Ticks.duration(20),
) {

    fun createTitle(vararg tagResolvers: TagResolver): Title = Title.title(
        title.resolve(*tagResolvers).asComponent(),
        subtitle.resolve(*tagResolvers).asComponent(),
        Title.Times.times(durationFadeIn, durationStay, durationFadeOut)
    )
}
