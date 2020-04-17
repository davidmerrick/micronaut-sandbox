package com.merricklabs.quarantinebot.external.slack.messages

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SlackEvent(
        val type: SlackEventType,
        val subtype: String?,
        val user: String,
        val text: String,
        val channel: String,
        val channelType: String,
        val botId: String?
)