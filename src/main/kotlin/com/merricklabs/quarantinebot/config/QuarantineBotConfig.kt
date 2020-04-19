package com.merricklabs.quarantinebot.config

import io.micronaut.context.annotation.ConfigurationProperties
import java.time.LocalDate
import javax.validation.constraints.NotBlank

@ConfigurationProperties("quarantineBot")
interface QuarantineBotConfig {
    @get:NotBlank
    val quarantineDate: LocalDate
}