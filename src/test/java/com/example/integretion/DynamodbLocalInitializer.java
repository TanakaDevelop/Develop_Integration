package com.example.integretion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.entity.UserEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

/**
 * DynamodbLocal初期化クラス
 */
public class DynamodbLocalInitializer {

	protected static final Path RESOURCE_DIR = Path.of("src/test/resources/");

	@Autowired
	private DynamoDbClient dynamoDbClient;

	@Autowired
	private DynamoDbEnhancedClient dynamoDbEnhancedClient;

	@Getter
	private enum Master {
		USER(UserEntity.class, "user");

		private final Class<?> beanClass;

		private final String tableName;

		Master(Class<?> beanClass, String tableName) {
			this.beanClass = beanClass;
			this.tableName = tableName;
		}
	}

	public <T> void setup() throws IOException {
		for (Master master : Master.values()) {
			final boolean isTableExists = dynamoDbClient.listTables().tableNames().contains(master.getTableName());

			if (!isTableExists) {
				// テーブルが存在しない場合は作成
				createTable(master);
			}
			inserData(master);
		}
	}

	private <T> void createTable(Master master) {
		@SuppressWarnings("unchecked")
		final Class<T> entityClass = (Class<T>) master.getBeanClass();

		// Table作成要求
		DynamoDbTable<T> table = dynamoDbEnhancedClient.table(master.getTableName(),
				TableSchema.fromBean(entityClass));
		table.createTable();
		DescribeTableRequest tableRequest = DescribeTableRequest.builder().tableName(master.getTableName()).build();
		DynamoDbWaiter dbWaiter = dynamoDbClient.waiter();
		WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
		waiterResponse.matched().response().ifPresent(response -> {
			System.out.println("Table " + master.getTableName() + " is created.");
		});

	}

	private <T> void inserData(Master master) throws IOException {

		String dbPath = "classpath:src/test/resources/dbData/" + master.getTableName() + ".json";

		String jsonData = readFile(dbPath);

		// JSON文字列をMapに変換
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonMap = mapper.readValue(jsonData, new TypeReference<>() {
		});

		// Map<String, AttributeValue> へ変換
		Map<String, AttributeValue> item = new HashMap<>();
		for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
			Object value = entry.getValue();
			AttributeValue attributeValue;

			if (value instanceof Number) {
				attributeValue = AttributeValue.builder().n(value.toString()).build();
			} else {
				attributeValue = AttributeValue.builder().s(value.toString()).build();
			}

			item.put(entry.getKey(), attributeValue);
		}

		// PutItem実行
		PutItemRequest request = PutItemRequest.builder()
				.tableName(master.getTableName())
				.item(item)
				.build();

		dynamoDbClient.putItem(request);

	}

	private String readFile(String fileName) throws IOException {
		Path target = Path.of(fileName);
		return Files.readString(target);
	}

}
