package com.merricklabs.quarantinebot.config

import io.micronaut.core.convert.ConversionContext
import io.micronaut.core.convert.TypeConverter
import java.lang.Exception
import java.time.LocalDate
import java.util.Optional
import javax.inject.Singleton

@Singleton
class LocalDateTypeConverter: TypeConverter<String, LocalDate> {
    override fun convert(dateString: String, targetType: Class<LocalDate>?, context: ConversionContext): Optional<LocalDate> {
        return try {
            Optional.of(LocalDate.parse(dateString))
        } catch (e: Exception){
            context.reject(e)
            Optional.empty()
        }
    }
}