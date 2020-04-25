package com.merricklabs.quarantinebot.testutil

import org.mockito.Mockito

object TestUtil {
    // Workaround for Mockito to allow null for any()
    fun <T> any(): T = Mockito.any<T>()
}