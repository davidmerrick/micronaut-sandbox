package com.merricklabs.quarantinebot.external.slack.client

import com.merricklabs.quarantinebot.external.slack.SlackPaths.BASE_API_PATH
import com.merricklabs.quarantinebot.external.slack.SlackPaths.POST_MESSAGE_ENDPOINT
import com.merricklabs.quarantinebot.external.slack.messages.CreateMessagePayload
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import mu.KotlinLogging
import javax.inject.Inject
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
class SlackClient {
    @field:Property(name = "slack.token")
    private lateinit var slackToken: String

    @Inject
    @field:Client(BASE_API_PATH)
    lateinit var client: RxHttpClient

    fun postMessage(payload: CreateMessagePayload){
        val request = HttpRequest.POST(
                POST_MESSAGE_ENDPOINT,
                payload
        )
        request.headers.add("Authorization", "Bearer $slackToken")
        val response = client.exchange(request)
                .blockingFirst()
        if(response.status != HttpStatus.OK){
            log.error("Received bad status from Slack: ${response.status}")
        } else {
            log.info("Success: Posted response to Slack")
        }
    }
}