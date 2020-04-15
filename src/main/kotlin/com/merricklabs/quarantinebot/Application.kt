package com.merricklabs.quarantinebot

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.jackson.JacksonConfiguration
import io.micronaut.jackson.ObjectMapperFactory
import io.micronaut.runtime.Micronaut
import javax.inject.Singleton

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.merricklabs.quarantinebot")
                .mainClass(Application.javaClass)
                .start()
    }

    @Factory
    @Replaces(ObjectMapperFactory::class)
    class CustomObjectMapperFactory : ObjectMapperFactory() {

        @Singleton
        @Replaces(ObjectMapper::class)
        override fun objectMapper(jacksonConfiguration: JacksonConfiguration?, jsonFactory: JsonFactory?): ObjectMapper {
            return super.objectMapper(jacksonConfiguration, jsonFactory)
                    .registerKotlinModule()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}