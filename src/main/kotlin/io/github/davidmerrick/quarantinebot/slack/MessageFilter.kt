package io.github.davidmerrick.quarantinebot.slack

import io.github.davidmerrick.slakson.messages.EventCallbackMessage
import io.github.davidmerrick.slakson.messages.SlackMessage
import javax.inject.Singleton

@Singleton
class MessageFilter {

    /**
     * Indicates whether the message should be acted upon
     * true if so, false if not
     */
    fun apply(message: SlackMessage): Boolean {
        return when(message){
            is EventCallbackMessage -> !message.isBotMessage()
            else -> true
        }
    }
}