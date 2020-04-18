package com.merricklabs.quarantinebot.util

private const val COUNTER_EMOJI = ":mask:"
private const val CHUNK_BY = 5

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