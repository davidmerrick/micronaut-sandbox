package com.merricklabs.quarantinebot.controllers

import com.merricklabs.quarantinebot.Application
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
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
    fun testPing() {
        val response = client.toBlocking()
                .retrieve("/slack")
        assertTrue(response.contains("bar"))
    }
}