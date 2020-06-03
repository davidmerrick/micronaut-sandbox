package io.github.davidmerrick.quarantinebot.slack

import io.github.davidmerrick.slakson.messages.EventCallbackMessage
import io.micronaut.caffeine.cache.Caffeine
import mu.KotlinLogging
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


private const val MAX_CACHE_SIZE = 100L
private const val CACHE_TTL_SECONDS = 30L

/**
 * This is necessary when run in a serverless environment;
 * When cold starts exceed Slack's retry threshold, Slack will retry,
 * resulting in the service receiving duplicate messages.
 */

private val log = KotlinLogging.logger {}

@Singleton
class SlackEventMessageCache {

    // Cache maps event id to a boolean
    private val cache = Caffeine.newBuilder()
            .maximumSize(MAX_CACHE_SIZE)
            .expireAfterWrite(CACHE_TTL_SECONDS, TimeUnit.SECONDS)
            .build<String, Boolean>()

    fun isMessageCached(message: EventCallbackMessage): Boolean {
        log.info("Cache size: ${cache.asMap().size}")
        return cache.asMap().putIfAbsent(message.eventId, true) != null
    }
}