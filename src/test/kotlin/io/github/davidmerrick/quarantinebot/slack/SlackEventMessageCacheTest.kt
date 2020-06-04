package io.github.davidmerrick.quarantinebot.slack

import io.github.davidmerrick.quarantinebot.TestApplication
import io.github.davidmerrick.slakson.messages.ChannelType
import io.github.davidmerrick.slakson.messages.EventCallbackMessage
import io.github.davidmerrick.slakson.messages.SlackEvent
import io.github.davidmerrick.slakson.messages.SlackEventType
import io.kotlintest.shouldBe
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest(application = TestApplication::class)
internal class SlackEventMessageCacheTest {

    @Inject
    lateinit var cache: SlackEventMessageCache

    @Test
    fun `Duplicate event id should be cached`(){
        val eventId = "Ev014R398W02"
        val message = EventCallbackMessage(
                token = "foo",
                teamId = "foo",
                apiAppId = "foo",
                event = SlackEvent(
                        type = SlackEventType.MESSAGE_CHANNELS,
                        subtype = "foo",
                        user = "foo",
                        text = "foo",
                        channel = "bar",
                        channelType = ChannelType.CHANNEL,
                        botId = null
                ),
                eventId = eventId,
                eventTime = 12345
        )

        cache.isMessageCached(message) shouldBe false
        cache.isMessageCached(message) shouldBe true
    }

    @Test
    fun `New event id should not be cached`(){
        val message = EventCallbackMessage(
                token = "foo",
                teamId = "foo",
                apiAppId = "foo",
                event = SlackEvent(
                        type = SlackEventType.MESSAGE_CHANNELS,
                        subtype = "foo",
                        user = "foo",
                        text = "foo",
                        channel = "bar",
                        channelType = ChannelType.CHANNEL,
                        botId = null
                ),
                eventId = "foo",
                eventTime = 12345
        )

        cache.isMessageCached(message) shouldBe false

        val newMessage = message.copy(eventId = "bar")
        cache.isMessageCached(newMessage) shouldBe false
    }
}