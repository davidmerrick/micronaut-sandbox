package com.merricklabs.quarantinebot.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.bind.annotation.Bindable
import javax.validation.constraints.NotBlank

@ConfigurationProperties("quarantineBot")
interface QuarantineBotConfig {
    @get:NotBlank
    @get:Bindable(defaultValue = "2020-03-11")
    val quarantineDate: String
}