package com.merricklabs.quarantinebot.external.slack

import com.merricklabs.quarantinebot.config.QuarantineBotConfig
import com.merricklabs.quarantinebot.external.slack.client.SlackClient
import com.merricklabs.quarantinebot.external.slack.messages.CreateMessagePayload
import com.merricklabs.quarantinebot.external.slack.messages.EventCallbackMessage
import com.merricklabs.quarantinebot.external.slack.messages.SlackChallenge
import com.merricklabs.quarantinebot.external.slack.messages.SlackMessage
import com.merricklabs.quarantinebot.util.OutputFormatter
import io.micronaut.http.HttpResponse
import mu.KotlinLogging
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Singleton
import kotlin.math.abs

private val log = KotlinLogging.logger {}

@Singleton
class SlackMessageDispatcher(
        private val slackClient: SlackClient,
        private val config: QuarantineBotConfig
) {
    fun dispatch(message: SlackMessage): HttpResponse<String> {
        log.info("Received Slack message of type ${message.type}")
        return when (message) {
            is SlackChallenge -> HttpResponse.ok(message.challenge)
            is EventCallbackMessage -> run {
                handleEventCallback(message)
                HttpResponse.ok("")
            }
            else -> run {
                log.warn("Unhandled Slack message type ${message.type}")
                HttpResponse.ok("")
            }
        }
    }

    private fun handleEventCallback(message: EventCallbackMessage) {
        log.info("Handling event callback message")
        if (message.event.subtype == "bot_message" || message.event.botId != null) {
            log.debug("Ignoring bot message")
            return
        }
        val replyText = if (message.event.text.toLowerCase().toRegex().matches("how long")) {
            val numDays = abs(ChronoUnit.DAYS.between(LocalDate.now(), config.quarantineDate))
            "It's been this many days:\n${OutputFormatter.printFormattedCount(numDays.toInt())}"
        } else {
            "Usage:\n `how long`: Print how long you've been quarantined."
        }

        val reply = CreateMessagePayload(
                message.event.channel,
                replyText
        )

        slackClient.postMessage(reply)
    }
}