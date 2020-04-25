package com.merricklabs.quarantinebot.controllers

import com.merricklabs.quarantinebot.TestApplication
import com.merricklabs.quarantinebot.external.slack.client.SlackClient
import io.kotlintest.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject
import kotlin.reflect.KClass


private const val EVENTS_ENDPOINT = "/slack/events"

@MicronautTest(application = TestApplication::class)
class SlackControllerTest {
    @Inject
    lateinit var server: EmbeddedServer

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

    @Disabled
    @Test
    fun `On message containing "how long?", should post a response to Slack`(){
        val payload = mapOf(
                "token" to "banana",
                "team_id" to "foo",
                "app_app_id" to "foo",
                "event" to mapOf(
                        "type" to "message",
                        "user" to "foo",
                        "text" to "how long",
                        "channel" to "banana",
                        "channel_type" to "banana"
                )
        )

        val request = HttpRequest.POST(
                EVENTS_ENDPOINT,
                payload
        )

        val response = client
                .retrieve(request)
                .blockingFirst()

    }

//    @MockBean(SlackClient::class)
//    private fun slackClient(): KClass<SlackClient>? = Mockito.spy(SlackClient)
}