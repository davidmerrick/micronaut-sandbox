package com.merricklabs.quarantinebot.external.slack.client

import io.micronaut.context.annotation.Property
import javax.inject.Singleton

@Singleton
class SlackClient {

    @field:Property(name = "slack.foo")
    lateinit var foo: String

    @field:Property(name = "slack.token")
    private lateinit var slackToken: String
}