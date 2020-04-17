package com.merricklabs.quarantinebot.external.slack.messages

data class SlackChallenge(
        val token: String,
        val challenge: String
) : SlackMessage {
    override val type: MessageType = MessageType.URL_VERIFICATION
}
