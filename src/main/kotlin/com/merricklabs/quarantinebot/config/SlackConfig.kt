package com.merricklabs.quarantinebot.config

import io.micronaut.context.annotation.ConfigurationProperties
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

/**
 * Previously, this class had been an interface,
 * but it didn't play well with GraalVM.
 * Ideally, these should be vals.
 */

@Singleton
@ConfigurationProperties("slack")
class SlackConfig {
    @get:NotBlank
    lateinit var token: String

    @get:NotBlank
    lateinit var botName: String
}