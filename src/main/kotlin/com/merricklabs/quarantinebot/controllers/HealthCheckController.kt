package com.merricklabs.quarantinebot.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/status")
class HealthCheckController {

    @Get("/up")
    fun up() = "OK"
}