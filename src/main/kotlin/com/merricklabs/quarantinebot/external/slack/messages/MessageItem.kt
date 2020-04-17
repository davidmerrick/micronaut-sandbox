package com.merricklabs.quarantinebot.external.slack.messages

data class MessageItem(
        val type: String,
        val channel: String,
        val ts: String
)