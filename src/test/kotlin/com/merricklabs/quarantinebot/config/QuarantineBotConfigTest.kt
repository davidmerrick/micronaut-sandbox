package com.merricklabs.quarantinebot.config

import com.merricklabs.quarantinebot.Application
import io.kotlintest.shouldBe
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test
import java.time.LocalDate
import javax.inject.Inject

@MicronautTest(application = Application::class)
class QuarantineBotConfigTest @Inject constructor(
        private val config: QuarantineBotConfig
) {
    @Test
    fun `Config should default to 2020-03-11 for quarantine date`(){
        config.quarantineDate shouldBe LocalDate.parse("2020-03-11")
    }

}