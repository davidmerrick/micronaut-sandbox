package io.github.davidmerrick.quarantinebot.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.davidmerrick.quarantinebot.TestApplication
import io.github.davidmerrick.quarantinebot.slack.MessageFilter
import io.github.davidmerrick.slakson.client.SlackClient
import io.github.davidmerrick.slakson.client.SlackClientImpl
import io.github.davidmerrick.slakson.messages.CreateMessagePayload
import io.github.davidmerrick.slakson.messages.EVENT_CALLBACK_STRING
import io.github.davidmerrick.slakson.messages.EventCallbackMessage
import io.github.davidmerrick.slakson.messages.SlackMessage
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
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
    lateinit var messageFilter: MessageFilter

    @Inject
    lateinit var mapper: ObjectMapper

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

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
                .toBlocking()
                .retrieve(request)

        response.contains(challenge) shouldBe true
    }

    @Test
    fun `Filter out bot messages`(){
        val payload = mapOf(
                "type" to "event_callback",
                "token" to "banana",
                "team_id" to "foo",
                "api_app_id" to "foo",
                "event" to mapOf(
                        "type" to "message",
                        "user" to "foo",
                        "text" to "how long",
                        "channel" to "banana",
                        "channel_type" to "channel",
                        "subtype" to "bot_message"
                )
        )

        val messageString = mapper.writeValueAsString(payload)
        val message = mapper.readValue<SlackMessage>(messageString)
        messageFilter.apply(message) shouldBe false
    }

    @Test
    fun `On message containing "how long?", should post a response to Slack`(){
        val payload = mapOf(
                "type" to "event_callback",
                "token" to "banana",
                "team_id" to "foo",
                "api_app_id" to "foo",
                "event" to mapOf(
                        "type" to "message",
                        "user" to "foo",
                        "text" to "how many",
                        "channel" to "banana",
                        "channel_type" to "im"
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
                .toBlocking()
                .retrieve(request, HttpStatus::class.java)

        status shouldBe HttpStatus.OK
        slot.captured.text.contains("this many days", true) shouldBe true
    }
}