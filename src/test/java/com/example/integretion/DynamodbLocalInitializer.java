package com.example.integretion;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import com.example.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

/**
 * DynamodbLocal初期化クラス
 */
public class DynamodbLocalInitializer {

	protected static final String TEST_DATA_FILE_PATH = "classpath:dbData/%s.json";

	@Autowired
	private DynamoDbClient dynamoDbClient;

	@Autowired
	private DynamoDbEnhancedClient dynamoDbEnhancedClient;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ObjectMapper objectMapper;

	@Getter
	private enum Master {
		USER(UserEntity.class, "User");

		private final Class<?> beanClass;

		private final String tableName;

		Master(Class<?> beanClass, String tableName) {
			this.beanClass = beanClass;
			this.tableName = tableName;
		}
	}

	@PostConstruct
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

		// テーブル名取得
		String tableName = master.getTableName();

		@SuppressWarnings("unchecked")
		final Class<T> entityClass = (Class<T>) master.getBeanClass();
		// テストデータ読み込み
		final String dataFilePath = String.format(TEST_DATA_FILE_PATH, tableName);
		final List<T> items = loadDataFromJson(dataFilePath, entityClass);
		// データ投入
		final DynamoDbTable<T> table = dynamoDbEnhancedClient.table(tableName,
				TableSchema.fromBean(entityClass));
		for (T item : items) {
			PutItemEnhancedRequest<T> putItemEnhancedRequest = PutItemEnhancedRequest.builder(entityClass)
					.item(item).build();
			table.putItem(putItemEnhancedRequest);
		}
	}

	private <T> List<T> loadDataFromJson(String filePath, Class<T> entityClass) throws IOException {
		try (InputStream inputStream = resourceLoader.getResource(filePath).getInputStream()) {
			return objectMapper.readValue(inputStream, objectMapper.getTypeFactory()
					.constructCollectionType(List.class, entityClass));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load data from JSON file: " + filePath, e);
		}
	}

}
