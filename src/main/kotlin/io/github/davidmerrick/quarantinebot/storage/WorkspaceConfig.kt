package io.github.davidmerrick.quarantinebot.storage

import io.micronaut.core.annotation.Introspected

@Introspected
data class WorkspaceConfig(
        val userId: String,
        val emoji: String
)