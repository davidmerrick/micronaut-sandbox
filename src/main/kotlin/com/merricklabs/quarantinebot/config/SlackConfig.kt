package com.merricklabs.quarantinebot.config

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties("slack")
interface SlackConfig {
    @get:NotBlank
    val token: String

    @get:NotBlank
    val botName: String
}