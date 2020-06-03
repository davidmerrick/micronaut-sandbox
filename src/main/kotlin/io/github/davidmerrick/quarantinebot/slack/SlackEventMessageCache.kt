package io.github.davidmerrick.quarantinebot.slack

import io.github.davidmerrick.slakson.messages.EventCallbackMessage
import io.micronaut.caffeine.cache.Caffeine
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


private const val MAX_CACHE_SIZE = 100L
private const val CACHE_TTL_SECONDS = 60L

/**
 * This is necessary when run in a serverless environment;
 * When cold starts exceed Slack's retry threshold, Slack will retry,
 * resulting in the service receiving duplicate messages.
 */
@Singleton
class SlackEventMessageCache {

    // Cache maps event id to a boolean
    private val cache = Caffeine.newBuilder()
            .maximumSize(MAX_CACHE_SIZE)
            .expireAfterWrite(CACHE_TTL_SECONDS, TimeUnit.SECONDS)
            .build<String, Boolean>()

    fun isMessageCached(message: EventCallbackMessage): Boolean {
        return cache.asMap().putIfAbsent(message.eventId, true) != null
    }
}