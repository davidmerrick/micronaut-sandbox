package com.merricklabs.quarantinebot.external.slack.messages

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventCallbackMessage(
        val token: String,
        @JsonProperty("team_id")
        val teamId: String,
        @JsonProperty("api_app_id")
        val apiAppId: String,
        val event: SlackEvent
) : SlackMessage {
    override val type = MessageType.EVENT_CALLBACK
}