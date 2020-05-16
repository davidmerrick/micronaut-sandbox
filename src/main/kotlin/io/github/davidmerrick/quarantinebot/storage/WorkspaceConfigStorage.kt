package io.github.davidmerrick.quarantinebot.storage

import io.github.davidmerrick.quarantinebot.config.QuarantineBotConfig
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import javax.inject.Singleton

/**
 * Stores per-workspace app config
 */

@Singleton
class WorkspaceConfigStorage(
        private val dynamoDbClient: DynamoDbClient,
        private val quarantineBotConfig: QuarantineBotConfig
) {

    /**
     * Store config for this user
     */
    fun storeConfig(payload: WorkspaceConfig) {
        dynamoDbClient.putItem {
            it.tableName(quarantineBotConfig.dynamoTableName)
            it.item(
                    mapOf(
                            "userId" to AttributeValue.builder().s(payload.userId).build(),
                            "emoji" to AttributeValue.builder().s(payload.emoji).build()
                    )
            )
        }
    }

    /**
     * Store config for this user
     */
    fun lookupConfig(userId: String): WorkspaceConfig? {
        val response = dynamoDbClient.getItem {
            it.tableName(quarantineBotConfig.dynamoTableName)
            it.key(mapOf("userId" to AttributeValue.builder().s(userId).build()))
        }
        response.item()?.let {
            return WorkspaceConfig(
                    userId,
                    it["emoji"]!!.s()
            )
        }

        return null
    }
}