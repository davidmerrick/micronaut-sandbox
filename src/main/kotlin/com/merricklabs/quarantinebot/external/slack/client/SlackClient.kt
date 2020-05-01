package com.merricklabs.quarantinebot.external.slack.client

import com.merricklabs.quarantinebot.external.slack.messages.CreateMessagePayload

interface SlackClient {
    fun postMessage(payload: CreateMessagePayload)
}