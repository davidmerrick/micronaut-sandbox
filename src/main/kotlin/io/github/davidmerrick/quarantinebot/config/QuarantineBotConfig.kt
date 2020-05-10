package io.github.davidmerrick.quarantinebot.config

import io.micronaut.context.annotation.ConfigurationProperties
import java.time.LocalDate
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@ConfigurationProperties("quarantineBot")
@Singleton
class QuarantineBotConfig {
    @get:NotBlank
    lateinit var quarantineDate: LocalDate

    @get:NotBlank
    lateinit var botName: String
}