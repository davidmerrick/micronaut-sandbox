package io.github.davidmerrick.quarantinebot.controllers

import io.github.davidmerrick.quarantinebot.TestApplication
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest(application = TestApplication::class)
class HealthCheckControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `Up endpoint should respond with OK`() {
        val response = client.toBlocking().retrieve("/status/up")
        assertEquals("OK", response)
    }
}