package com.merricklabs.quarantinebot.external.slack.client

import com.merricklabs.quarantinebot.external.slack.messages.CreateMessagePayload
import io.micronaut.aop.Introduction
import io.micronaut.core.annotation.Introspected

@Introduction
@Introspected
interface SlackClient {
    fun postMessage(payload: CreateMessagePayload)
}