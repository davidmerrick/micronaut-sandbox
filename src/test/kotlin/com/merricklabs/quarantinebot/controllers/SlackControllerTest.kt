package com.merricklabs.quarantinebot.controllers

import com.merricklabs.quarantinebot.TestApplication
import com.merricklabs.quarantinebot.external.slack.client.SlackClient
import com.merricklabs.quarantinebot.external.slack.messages.CreateMessagePayload
import com.merricklabs.quarantinebot.external.slack.messages.EVENT_CALLBACK_STRING
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Test
import javax.inject.Inject

private const val EVENTS_ENDPOINT = "/slack/events"

@MicronautTest(application = TestApplication::class)
class SlackControllerTest {

    @get:MockBean(SlackClient::class)
    val slackClient = mockk<SlackClient>()

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Test
    fun `Handle Slack challenge`() {
        val challenge = "foo"
        val payload = mapOf(
                "challenge" to challenge,
                "token" to "banana",
                "type" to "url_verification"
        )
        val request = HttpRequest.POST(
                EVENTS_ENDPOINT,
                payload
        )

        val response = client
                .retrieve(request)
                .blockingFirst()

        response.contains(challenge) shouldBe true
    }

    @Test
    fun `On message containing "how long?", should post a response to Slack`(){
        val payload = mapOf(
                "type" to EVENT_CALLBACK_STRING,
                "token" to "banana",
                "team_id" to "foo",
                "api_app_id" to "foo",
                "event" to mapOf(
                        "type" to "message",
                        "user" to "foo",
                        "text" to "how long",
                        "channel" to "banana",
                        "channel_type" to "banana"
                )
        )

        val slot = slot<CreateMessagePayload>()
        every {
            slackClient.postMessage(capture(slot))
        } just Runs

        val request = HttpRequest.POST(
                EVENTS_ENDPOINT,
                payload
        )

        val status = client
                .retrieve(request, HttpStatus::class.java)
                .blockingFirst()

        status shouldBe HttpStatus.OK
        slot.captured.text.toLowerCase() shouldContain "this many days"
    }
}