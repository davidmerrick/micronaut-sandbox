package com.merricklabs.quarantinebot.client

import com.merricklabs.quarantinebot.external.slack.client.SlackClient
import com.merricklabs.quarantinebot.external.slack.messages.CreateMessagePayload
import javax.inject.Singleton

@Singleton
open class MockSlackClient: SlackClient() {
    override fun postMessage(payload: CreateMessagePayload) {
        TODO("Not yet implemented")
    }
}