package com.merricklabs.quarantinebot.controllers

import com.merricklabs.quarantinebot.Application
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest(application = Application::class)
class SlackControllerTest {
    @Inject
    lateinit var server: EmbeddedServer

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Test
    fun `Handle Slack challenge`() {
        val challenge = mapOf(
                "challenge" to "foo",
                "token" to "banana",
                "type" to "url_verification"
        )
        val request = HttpRequest.POST(
                "/slack/events",
                challenge
        )

        val response = client.exchange(request)
                .blockingFirst()
        val body = String(response.body()!!.toByteArray())
        assertTrue(body.contains("foo"))
        assertEquals(response.status, HttpStatus.OK)
    }
}