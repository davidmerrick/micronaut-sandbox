package io.github.davidmerrick.quarantinebot.storage

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.core.annotation.Introspected
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Introspected
@Factory
class DynamoClientFactory {

    @Bean
    fun dynamoClient(): DynamoDbClient {
        return DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .build()
    }
}