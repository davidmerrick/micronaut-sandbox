package com.merricklabs.quarantinebot.external.slack.client

import com.merricklabs.quarantinebot.external.slack.messages.CreateMessagePayload

abstract class SlackClient {
    open abstract fun postMessage(payload: CreateMessagePayload)
}