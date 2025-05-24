package com.example.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@ConditionalOnProperty(value = "aws.dynamodb.enable", havingValue = "true", matchIfMissing = false)
public class DynamoDbConfig {

	@Value("${aws.dynamodb.endpoint}")
	private String endpoint;

	@Value("${aws.dynamodb.accessKey}")
	private String accessKey;

	@Value("${aws.dynamodb.secretKey}")
	private String secretKey;

	/**
	 * Dynamodb初期設定
	 *
	 * @return DynamoDbClient
	 */
	@Bean
	DynamoDbClient dynamoDbClient() {
		// AWS DynamoDbClientの初期化
		return DynamoDbClient.builder()
				.endpointOverride(URI.create(endpoint))
				.region(Region.US_EAST_2) // The region is meaningless for local DynamoDb but required for client builder validation
				.credentialsProvider(StaticCredentialsProvider
						.create(AwsBasicCredentials.create(accessKey, secretKey)))
				.build();
	}

}
