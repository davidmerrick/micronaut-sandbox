package com.merricklabs.quarantinebot.external.slack.client

interface SlackResponse {
    val ok: Boolean
    val error: String?
}