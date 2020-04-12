package com.merricklabs.quarantinebot

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/events")
class SlackController {

    @Get("/")
    fun index() = "hello world"
}