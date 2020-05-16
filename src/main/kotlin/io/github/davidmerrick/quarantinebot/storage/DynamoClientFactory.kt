package io.github.davidmerrick.quarantinebot.storage

import io.micronaut.context.annotation.Factory
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import javax.inject.Singleton

@Factory
class DynamoClientFactory {

    // Todo: Update cred provider here
    @Singleton
    fun dynamoClient(): DynamoDbClient {
        return DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider {
                    ProfileCredentialsProvider.create("terraform-sandbox").resolveCredentials()
                }.build()
    }
}