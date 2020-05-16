package io.github.davidmerrick.quarantinebot.slack

import io.github.davidmerrick.quarantinebot.config.QuarantineBotConfig
import io.github.davidmerrick.quarantinebot.storage.WorkspaceConfig
import io.github.davidmerrick.quarantinebot.storage.WorkspaceConfigStorage
import io.github.davidmerrick.quarantinebot.util.DEFAULT_EMOJI
import io.github.davidmerrick.quarantinebot.util.OutputFormatter
import io.github.davidmerrick.slakson.client.SlackClient
import io.github.davidmerrick.slakson.messages.ChannelType.CHANNEL
import io.github.davidmerrick.slakson.messages.ChannelType.GROUP
import io.github.davidmerrick.slakson.messages.ChannelType.IM
import io.github.davidmerrick.slakson.messages.CreateMessagePayload
import io.github.davidmerrick.slakson.messages.SlackEvent
import io.github.davidmerrick.slakson.messages.SlackEventType
import mu.KotlinLogging
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Singleton
import kotlin.math.abs

private val log = KotlinLogging.logger {}

@Singleton
class SlackEventHandler(
        private val slackClient: SlackClient,
        private val config: QuarantineBotConfig,
        private val storage: WorkspaceConfigStorage
) {
    private val usageText = """
                Usage:
                    * `how many`: Print how many days you've been quarantined.
                    * `set emoji <emoji>`: Set the emoji bot should use.
            """.trimIndent()
    private val setEmojiRegex = "^set emoji (:[a-zA-Z_-]*:)".toRegex()

    fun handle(event: SlackEvent): String? {
        log.info("Handling Slack event")
        when (event.channelType) {
            CHANNEL, GROUP -> handleChannelEvent(event)
            IM -> handleImEvent(event)
            else -> fallbackHandler(event)
        }
        return null
    }

    private fun handleChannelEvent(event: SlackEvent) {
        log.info("Handling channel event")
        if (event.type != SlackEventType.APP_MENTION && !event.text.contains(config.botName, true)) {
            log.info("Text does not contain ${config.botName}, skipping")
            return
        }

        postReply(event)
    }

    private fun handleImEvent(event: SlackEvent) {
        log.info("Handling IM event")
        postReply(event)
    }

    private fun fallbackHandler(event: SlackEvent) {
        log.info("Fallback event handler")
        // Handle event in same way as a channel event
        handleChannelEvent(event)
    }

    private fun postReply(event: SlackEvent) {
        val replyText = getReplyText(event)

        log.info("Posting reply: $replyText")
        val reply = CreateMessagePayload(
                event.channel,
                replyText
        )

        slackClient.postMessage(reply)
    }

    private fun getReplyText(event: SlackEvent): String {
        log.info("Handling event with text: ${event.text}")
        return when {
            event.text.contains("how many", true) -> {
                val numDays = abs(ChronoUnit.DAYS.between(LocalDate.now(), config.quarantineDate))
                val config = storage.lookupConfig(event.user)
                val emoji = config?.emoji ?: DEFAULT_EMOJI
                "It's been this many days:\n${OutputFormatter.printFormattedCount(numDays.toInt(), emoji)}"
            }
            setEmojiRegex.matches(event.text) -> {
                val emoji = setEmojiRegex.find(event.text)?.groups?.get(1)?.value
                emoji?.let {
                    val payload = WorkspaceConfig(event.user, it)
                    storage.storeConfig(payload)
                }
                "Set emoji to $emoji"
            }
            else -> usageText
        }
    }
}