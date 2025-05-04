package com.example.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.entity.UserEntity;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

@ExtendWith(MockitoExtension.class)
class UserForDynamodbTest {

	@Mock
	private DynamoDbClient dynamoDbClient;

	@InjectMocks
	private UserForDynamodb userForDynamodb;

	@DisplayName("DynamoDBからのクエリ結果を正しく取得できること")
	@Test
	void test1() {
		Map<String, AttributeValue> item1 = new HashMap<>();
		item1.put("id", AttributeValue.fromS("user1"));
		Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
		lastEvaluatedKey.put("id", AttributeValue.fromS("user1"));

		QueryResponse response1 = QueryResponse.builder()
				.items(item1)
				.lastEvaluatedKey(lastEvaluatedKey)
				.build();

		// ページ2（最終ページ）
		Map<String, AttributeValue> item2 = new HashMap<>();
		item2.put("id", AttributeValue.fromS("user2"));

		QueryResponse response2 = QueryResponse.builder()
				.items(item2)
				.lastEvaluatedKey(Collections.emptyMap())
				.build();

		// モックの設定（2回呼ばれる想定）
		when(dynamoDbClient.query(any(QueryRequest.class)))
				.thenReturn(response1)
				.thenReturn(response2);

		// 実行
		List<UserEntity> result = userForDynamodb.query("user1");

		// 検証
		assertEquals(2, result.size());
		assertEquals("user1", result.get(0).getId());
		assertEquals("user2", result.get(1).getId());

		verify(dynamoDbClient, times(2)).query(any(QueryRequest.class));
	}

	@DisplayName("DynamoDBからのクエリ結果を正しく取得できないこと")
	@Test
	void test2() {
		Map<String, AttributeValue> item1 = new HashMap<>();
		item1.put("id", AttributeValue.fromS("user2"));
		Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
		lastEvaluatedKey.put("id", AttributeValue.fromS("user2"));

		QueryResponse response1 = QueryResponse.builder()
				.items(item1)
				.lastEvaluatedKey(lastEvaluatedKey)
				.build();

		// ページ2（最終ページ）
		Map<String, AttributeValue> item2 = new HashMap<>();
		item2.put("id", AttributeValue.fromS("user2"));

		QueryResponse response2 = QueryResponse.builder()
				.items(item2)
				.lastEvaluatedKey(Collections.emptyMap())
				.build();

		// モックの設定（2回呼ばれる想定）
		when(dynamoDbClient.query(any(QueryRequest.class)))
				.thenReturn(response1)
				.thenReturn(response2);

		// 実行
		List<UserEntity> result = userForDynamodb.query("user1");

		// 検証
		assertEquals(2, result.size());
		assertEquals("user1", result.get(0).getId());
		assertEquals("user2", result.get(1).getId());

		verify(dynamoDbClient, times(2)).query(any(QueryRequest.class));
	}

}
