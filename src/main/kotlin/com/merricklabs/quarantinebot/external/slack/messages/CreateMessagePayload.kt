package com.merricklabs.quarantinebot.external.slack.messages

data class CreateMessagePayload(
        val channel: String,
        val text: String,
        val threadTs: String? = null
)