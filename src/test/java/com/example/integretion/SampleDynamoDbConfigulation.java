//package com.example.integretion;
//
//@TestConfiguration
//@Testcontainers
//public class SampleDynamoDbConfigulation {
//
//	@SuppressWarnings("resource")
//	@Container
//	private final LocalStackContainer localStack = new LocalStackContainer(
//			DockerImageName.parse("localstack/localstack:3.8"))
//					.withServices(LocalStackContainer.Service.DYNAMODB);
//
//	/**
//	 * Dynamodb初期設定
//	 *
//	 * @return DynamoDbClient
//	 */
//	@Bean
//	DynamoDbClient dynamoDbClient() {
//		// AWS DynamoDbClientの初期化
//		localStack.start();
//		return DynamoDbClient.builder()
//				.credentialsProvider(StaticCredentialsProvider.create(
//						AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
//				.endpointOverride(
//						URI.create(localStack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString()))
//				.build();
//	}
//}
