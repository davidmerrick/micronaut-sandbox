package com.merricklabs.quarantinebot.config

import io.micronaut.context.annotation.ConfigurationProperties
import java.time.LocalDate
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@ConfigurationProperties("quarantineBot")
@Singleton
// Todo: Make this an interface when you can figure out
// how to get it to play nice with Graal
class QuarantineBotConfig {
    @get:NotBlank
    lateinit var quarantineDate: LocalDate
}