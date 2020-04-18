package com.merricklabs.quarantinebot.external.slack.client

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SlackResponseImpl(
        override val ok: Boolean,
        override val error: String?
) : SlackResponse