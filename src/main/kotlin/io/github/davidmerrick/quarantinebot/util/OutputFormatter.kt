package io.github.davidmerrick.quarantinebot.util

import io.micronaut.core.annotation.Introspected

const val DEFAULT_EMOJI = ":mask:"
private const val CHUNK_BY = 5

@Introspected
object OutputFormatter {
    fun printFormattedCount(numDays: Int, emoji: String): String {
        return (0 until numDays)
                .asSequence()
                .map { emoji }
                .chunked(CHUNK_BY)
                .map { it.joinToString("") }
                .joinToString("\n")
    }
}