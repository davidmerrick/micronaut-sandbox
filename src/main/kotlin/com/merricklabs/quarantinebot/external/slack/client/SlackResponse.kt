package com.merricklabs.quarantinebot.external.slack.client

import io.micronaut.core.annotation.Introspected

@Introspected
interface SlackResponse {
    val ok: Boolean
    val error: String?
}