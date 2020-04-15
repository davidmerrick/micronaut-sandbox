package com.merricklabs.quarantinebot.external.slack

data class SlackChallenge(
        val token: String,
        val challenge: String,
        val type: String
)
