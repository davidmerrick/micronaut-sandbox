package com.merricklabs.quarantinebot

import com.fasterxml.jackson.databind.ObjectMapper
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

    @Test
    fun `Should exclude fields with null values from serialization`() {
        val myClass = MyClass(foo = "banana", bar = null)
        val result = mapper.writeValueAsString(myClass)
        result.contains("bar") shouldBe false
    }
}