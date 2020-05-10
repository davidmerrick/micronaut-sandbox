package io.github.davidmerrick.quarantinebot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.davidmerrick.slakson.messages.ChannelType
import io.kotlintest.shouldBe
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test
import javax.inject.Inject


@MicronautTest(application = TestApplication::class)
class ObjectMapperTest {

    @Inject
    lateinit var mapper: ObjectMapper

    private data class MyClass(
            val foo: String,
            val bar: String?
    )

    private data class CasedEnums(
            val foo: ChannelType
    )

    @Test
    fun `Mapper should exclude fields with null values from serialization`() {
        val myClass = MyClass(foo = "banana", bar = null)
        val result = mapper.writeValueAsString(myClass)
        result.contains("bar") shouldBe false
    }

    @Test
    fun `Mapper should be case-insensitive with enums`() {
        val payload = mapper.writeValueAsString(mapOf("foo" to "channel"))
        val deserialized = mapper.readValue<CasedEnums>(payload)
        deserialized.foo shouldBe ChannelType.CHANNEL
    }
}