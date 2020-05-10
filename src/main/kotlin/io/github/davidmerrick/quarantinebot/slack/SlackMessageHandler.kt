package io.github.davidmerrick.quarantinebot.slack

import io.github.davidmerrick.slakson.messages.EventCallbackMessage
import io.github.davidmerrick.slakson.messages.SlackChallenge
import io.github.davidmerrick.slakson.messages.SlackMessage
import mu.KotlinLogging
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
class SlackMessageHandler(
        private val eventHandler: SlackEventHandler
) {
    fun handle(message: SlackMessage): String? {
        log.info("Received Slack message of type ${message.type}")
        when (message) {
            is SlackChallenge -> run {
                log.info("Handling Slack challenge message")
                return message.challenge
            }
            is EventCallbackMessage -> run {
                log.info("Handling event callback message")
                if (message.isBotMessage()) {
                    log.info("Skipping bot message")
                    return null
                }
                eventHandler.handle(message.event)
            }
            else -> run {
                log.warn("Unhandled Slack message type ${message.type}")
            }
        }
        return null
    }
}