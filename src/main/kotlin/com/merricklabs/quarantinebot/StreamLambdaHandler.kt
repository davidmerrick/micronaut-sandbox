package com.merricklabs.quarantinebot

import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import io.micronaut.function.aws.proxy.MicronautLambdaContainerHandler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class StreamLambdaHandler : RequestStreamHandler {
    private val handler: MicronautLambdaContainerHandler

    @Throws(IOException::class)
    override fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context) {
        handler.proxyStream(inputStream, outputStream, context)
    }

    init {
        try {
            handler = MicronautLambdaContainerHandler()
        } catch (e: ContainerInitializationException) { // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace()
            throw RuntimeException("Could not initialize Micronaut", e)
        }
    }
}