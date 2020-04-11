package com.merricklabs

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

// Reference: https://docs.micronaut.io/latest/guide/index.html#runningServer

@MicronautTest
class PingControllerTest {
    @Inject
    lateinit var server: EmbeddedServer

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testPing() {
        val response = client.toBlocking()
                .retrieve("/ping")
        assertEquals("Hello World", response) //)

    }
}