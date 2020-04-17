package com.merricklabs.quarantinebot.external.slack.messages

import com.fasterxml.jackson.annotation.JsonProperty

enum class SlackEventType {
    @JsonProperty("message")
    MESSAGE
}