package com.merricklabs.quarantinebot

import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.inject.Inject

// Reference: https://docs.micronaut.io/latest/guide/index.html#runningServer

@MicronautTest(application = Application::class)
class PingControllerTest {
    @Inject
    lateinit var server: EmbeddedServer

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Test
    fun testPing() {
        val response = client.toBlocking()
                .retrieve("/ping")
        assertTrue(response.contains("pong"))

    }
}