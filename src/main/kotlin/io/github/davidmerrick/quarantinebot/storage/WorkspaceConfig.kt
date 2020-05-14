package io.github.davidmerrick.quarantinebot.storage

import io.micronaut.core.annotation.Introspected

@Introspected
data class WorkspaceConfig(
        val teamId: String,
        val emoji: String
)