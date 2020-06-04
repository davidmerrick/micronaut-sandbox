package io.github.davidmerrick.quarantinebot.util

import io.micronaut.core.annotation.Introspected

private const val COUNTER_EMOJI = ":tiger:"
private const val CHUNK_BY = 5

@Introspected
object OutputFormatter {
    fun printFormattedCount(numDays: Int): String {
        return (0 until numDays)
                .asSequence()
                .map { COUNTER_EMOJI }
                .chunked(CHUNK_BY)
                .map { it.joinToString("") }
                .joinToString("\n")
    }
}