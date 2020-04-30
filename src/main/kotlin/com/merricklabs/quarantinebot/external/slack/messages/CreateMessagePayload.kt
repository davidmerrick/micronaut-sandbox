package com.merricklabs.quarantinebot.external.slack.messages

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected

@Introspected
data class CreateMessagePayload(
        @JsonProperty("channel")
        val channel: String,
        @JsonProperty("text")
        val text: String,
        @JsonProperty("thread_ts")
        val threadTs: String? = null
)