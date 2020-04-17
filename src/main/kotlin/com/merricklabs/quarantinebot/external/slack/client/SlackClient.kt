package com.merricklabs.quarantinebot.external.slack.client

import io.micronaut.context.annotation.Property
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import java.io.IOException
import javax.inject.Singleton

@Singleton
class SlackClient {
    @field:Property(name = "slack.token")
    private lateinit var slackToken: String

//    private val client = OkHttpClient()

//    fun postToSlack(){
//        val request = Request.Builder()
//                .url("https://publicobject.com/helloworld.txt")
//                .build()
//
//        client.newCall(request).execute().use { response ->
//            if (!response.isSuccessful) throw IOException("Unexpected code $response")
//        }
//
//    }
}