package com.merricklabs.quarantinebot.controllers

import com.merricklabs.quarantinebot.external.slack.messages.SlackMessage
import com.merricklabs.quarantinebot.slack.SlackMessageHandler
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("/slack")
class SlackController(private val handler: SlackMessageHandler) {

    @Post("/events",
            consumes = [MediaType.APPLICATION_JSON],
            produces = [MediaType.TEXT_PLAIN]
    )
    fun handleEvent(@Body message: SlackMessage): String {
        return handler.handle(message) ?: ""
    }
}