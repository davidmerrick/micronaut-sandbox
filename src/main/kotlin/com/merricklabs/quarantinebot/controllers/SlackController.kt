package com.merricklabs.quarantinebot.controllers

import com.merricklabs.quarantinebot.external.slack.SlackChallenge
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces

@Controller("/slack")
class SlackController {

    @Post("/events")
    @Produces(MediaType.TEXT_PLAIN)
    fun handleEvent(@Body event: SlackChallenge): String {
        return event.challenge
    }
}