package com.merricklabs.quarantinebot.external.slack.client

import com.merricklabs.quarantinebot.config.SlackConfig
import com.merricklabs.quarantinebot.external.slack.SlackPaths.BASE_API_PATH
import com.merricklabs.quarantinebot.external.slack.SlackPaths.POST_MESSAGE_ENDPOINT
import com.merricklabs.quarantinebot.external.slack.messages.CreateMessagePayload
import io.micronaut.aop.Introduction
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import mu.KotlinLogging
import javax.inject.Inject
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
@Introspected
@Introduction(interfaces = [SlackClient::class])
class SlackClientImpl
@Inject constructor(
        private val slackConfig: SlackConfig,
        private val client: HttpClient
) : SlackClient {
    override fun postMessage(payload: CreateMessagePayload) {
        val request = HttpRequest.POST(
                BASE_API_PATH + POST_MESSAGE_ENDPOINT,
                payload
        )
        request.headers.add("Authorization", "Bearer ${slackConfig.token}")
        val response = client.toBlocking().retrieve(request, HttpStatus::class.java)
        if (response != HttpStatus.OK) {
            log.error("Received bad status from Slack: $response")
        } else {
            log.info("Success: Posted response to Slack")
        }
    }
}