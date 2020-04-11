package com.merricklabs.quarantinebot

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/ping")
class PingController {

    @Get("/")
    fun index(): String {
        return "{\"pong\":true}"
    }
}