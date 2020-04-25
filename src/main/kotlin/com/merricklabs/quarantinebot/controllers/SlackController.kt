package com.merricklabs.quarantinebot.controllers

import com.merricklabs.quarantinebot.external.slack.SlackMessageDispatcher
import com.merricklabs.quarantinebot.external.slack.messages.SlackMessage
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import javax.inject.Inject

@Controller("/slack")
class SlackController @Inject constructor(
        private val dispatcher: SlackMessageDispatcher
) {

    @Post("/events")
    @Produces(MediaType.TEXT_PLAIN)
    fun handleEvent(@Body message: SlackMessage): HttpResponse<String> {
        return dispatcher.dispatch(message)
    }
}