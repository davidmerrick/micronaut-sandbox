package com.merricklabs.quarantinebot.external.slack

import com.merricklabs.quarantinebot.external.slack.messages.SlackChallenge
import com.merricklabs.quarantinebot.external.slack.messages.SlackMessage
import io.micronaut.http.HttpResponse
import mu.KotlinLogging
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
class SlackMessageDispatcher {

    fun dispatch(message: SlackMessage): HttpResponse<String> {
        return when (message) {
            is SlackChallenge -> HttpResponse.ok(message.challenge)
            else -> run {
                log.warn("Unhandled Slack message type ${message.type}")
                HttpResponse.ok("")
            }
        }
    }
}