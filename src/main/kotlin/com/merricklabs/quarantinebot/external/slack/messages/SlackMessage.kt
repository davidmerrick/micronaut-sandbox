package com.merricklabs.quarantinebot.external.slack.messages

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.micronaut.core.annotation.Introspected

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = SlackChallenge::class, name = URL_VERIFICATION_STRING),
        JsonSubTypes.Type(value = EventCallbackMessage::class, name = EVENT_CALLBACK_STRING)
)
@Introspected
interface SlackMessage {
    val type: MessageType
}