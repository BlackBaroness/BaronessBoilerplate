package io.github.blackbaroness.boilerplates.paper

import org.bukkit.command.CommandSender

interface UserExceptionHandler {
    suspend fun handle(user: CommandSender, error: Throwable)
}