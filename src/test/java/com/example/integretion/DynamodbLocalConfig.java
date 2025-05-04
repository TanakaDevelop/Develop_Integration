package com.example.integretion;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamodbLocalConfig {

	/**
	 * Dynamodb初期設定
	 *
	 * @return DynamoDbClient
	 */
	@Bean
	DynamoDbClient dynamoDbClient() {
		// AWS DynamoDbClientの初期化
		return DynamoDbClient.builder()
				.endpointOverride(URI.create("http://localhost:8000"))
				.region(Region.US_EAST_2)
				.credentialsProvider(StaticCredentialsProvider
						.create(AwsBasicCredentials.create("dummy", "dummy")))
				.build();
	}

	@Bean
	DynamoDbEnhancedClient dynamoDbEnhancedClient() {
		return DynamoDbEnhancedClient.builder()
				.dynamoDbClient(dynamoDbClient())
				.build();
	}

}
