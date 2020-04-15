package com.merricklabs.quarantinebot.controllers

import com.merricklabs.quarantinebot.controllers.models.SlackEvent
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/slack")
class SlackController {

    @Get("/")
    fun index() = SlackEvent("bar")
}