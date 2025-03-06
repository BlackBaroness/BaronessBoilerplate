package io.github.blackbaroness.boilerplate.litecommands.argument

import dev.rollczi.litecommands.argument.Argument
import dev.rollczi.litecommands.argument.parser.ParseResult
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages
import dev.rollczi.litecommands.invocation.Invocation
import dev.rollczi.litecommands.message.MessageRegistry
import dev.rollczi.litecommands.suggestion.SuggestionContext
import dev.rollczi.litecommands.suggestion.SuggestionResult
import jakarta.inject.Inject
import org.bukkit.Server
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BoilerPlayerArgument @Inject constructor(
    private val server: Server,
    private val messageRegistry: MessageRegistry<CommandSender>
) : ArgumentResolver<CommandSender, Player>() {

    override fun parse(
        invocation: Invocation<CommandSender>,
        context: Argument<Player>,
        argument: String
    ): ParseResult<Player> {
        val sender = invocation.sender()
        val player = server.getPlayer(argument)

        if (player == null || (sender is Player && !sender.canSee(player))) {
            return ParseResult.failure(
                messageRegistry.getInvoked(
                    LiteBukkitMessages.PLAYER_NOT_FOUND,
                    invocation,
                    argument
                )
            )
        }

        return ParseResult.success(player)
    }

    override fun suggest(
        invocation: Invocation<CommandSender>,
        argument: Argument<Player>,
        context: SuggestionContext
    ): SuggestionResult {
        val sender = invocation.sender()
        val suggestions = server.onlinePlayers
            .filter { sender !is Player || sender.canSee(it) }
            .map { it.name }

        return SuggestionResult.of(suggestions)
    }
}
