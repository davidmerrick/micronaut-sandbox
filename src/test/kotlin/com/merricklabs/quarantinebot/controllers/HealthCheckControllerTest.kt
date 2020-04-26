package com.merricklabs.quarantinebot.controllers

import com.merricklabs.quarantinebot.TestApplication
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest(application = TestApplication::class)
class HealthCheckControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Test
    fun `Up endpoint should respond with OK`() {
        val response = client.toBlocking().retrieve("/status/up")
        assertEquals("OK", response)
    }
}