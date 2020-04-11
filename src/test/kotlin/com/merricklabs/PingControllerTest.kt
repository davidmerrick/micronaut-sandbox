package com.merricklabs

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class PingControllerTest {
    @Inject
    lateinit var embeddedServer: EmbeddedServer

    // Todo: Test isn't currently working
    @Test
    @Throws(Exception::class)
    fun testPing() {
        embeddedServer.applicationContext.createBean(RxHttpClient::class.java, embeddedServer.url)
                .use { client ->
                    Assertions.assertEquals(
                            HttpStatus.OK,
                            client.toBlocking().exchange<Any>("/ping").status())
                }
    }
}