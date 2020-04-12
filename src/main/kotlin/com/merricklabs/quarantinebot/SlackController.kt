package com.merricklabs.quarantinebot

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import javax.ws.rs.core.MediaType

@Controller("/events")
class SlackController {

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun index() = "hello world"
}