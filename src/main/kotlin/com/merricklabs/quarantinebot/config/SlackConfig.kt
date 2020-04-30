package com.merricklabs.quarantinebot.config

import io.micronaut.aop.Introduction
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@ConfigurationProperties("slack")
@Introduction
@Introspected
interface SlackConfig {
    @get:NotBlank
    val token: String

    @get:NotBlank
    val botName: String
}