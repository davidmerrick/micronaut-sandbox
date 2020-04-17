package com.merricklabs.quarantinebot.external.slack.messages

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventCallbackMessage(
        val token: String,
        val teamId: String,
        val apiAppId: String,
        val event: SlackEvent
) : SlackMessage {
    override val type = MessageType.EVENT_CALLBACK
}