package com.merricklabs.quarantinebot.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@ConfigurationProperties("slack")
interface SlackConfig {
    @get:NotBlank
    val token: String

    @get:NotBlank
    val botName: String
}