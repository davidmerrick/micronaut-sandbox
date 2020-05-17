package io.github.davidmerrick.quarantinebot.storage

import io.micronaut.core.annotation.Introspected
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import javax.inject.Singleton

@Introspected
@Singleton
class DynamoClientFactory {

    fun dynamoClient(): DynamoDbClient {
        return DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .build()
    }
}