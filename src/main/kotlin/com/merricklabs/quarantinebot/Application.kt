package com.merricklabs.quarantinebot

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.merricklabs.quarantinebot")
                .mainClass(Application.javaClass)
                .start()
    }
}