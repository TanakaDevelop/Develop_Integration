//package com.example.integretion;
//
//import static org.assertj.core.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//
//import com.example.entity.UserEntity;
//import com.example.repository.UserForDynamodb;
//
//import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
//import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
//import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
//import software.amazon.awssdk.services.dynamodb.model.BillingMode;
//import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
//import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
//import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
//import software.amazon.awssdk.services.dynamodb.model.KeyType;
//import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
//import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
//import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
//
//@SpringBootTest
//@Import(SampleDynamoDbConfigulation.class)
//class SampleTest {
//
//	@Autowired
//	UserForDynamodb userForDynamodb;
//
//	@Autowired
//	private DynamoDbClient dynamoDbClient;
//
//	@BeforeEach
//	public void setUpAll() throws Exception {
//		createTable();
//	}
//
//	private void createTable() {
//		String tableName = "User";
//
//		// テーブルを作成
//		CreateTableRequest request = CreateTableRequest.builder()
//				.tableName(tableName)
//				.keySchema(KeySchemaElement.builder()
//						.attributeName("id")
//						.keyType(KeyType.HASH)
//						.build())
//				.attributeDefinitions(AttributeDefinition.builder()
//						.attributeName("id")
//						.attributeType(ScalarAttributeType.S)
//						.build())
//				.billingMode(BillingMode.PAY_PER_REQUEST)
//				.build();
//
//		CreateTableResponse response = dynamoDbClient.createTable(request);
//		System.out.println(response.tableDescription());
//
//		List<UserEntity> userList = query("1");
//		System.out.println(userList);
//	}
//
//	/**
//	* query 指定のパーテンションキーから値を取得する
//	*
//	* @param id ID
//	* @return List<UserEntity>
//	*/
//	public List<UserEntity> query(String id) {
//
//		//　検索キー項目名を設定
//		HashMap<String, String> attrNameAlias = new HashMap<String, String>();
//		attrNameAlias.put("#id", "id");
//		// 検索項目値を設定
//		HashMap<String, AttributeValue> attrValues = new HashMap<>();
//		attrValues.put(":id", AttributeValue.builder()
//				.s(id)
//				.build());
//		// 検索クエリビルダ
//		QueryRequest.Builder queryReq = QueryRequest.builder()
//				.tableName("User")
//				.keyConditionExpression("#id = :id")
//				.expressionAttributeNames(attrNameAlias)
//				.expressionAttributeValues(attrValues);
//
//		// 結果のページングを管理するために使用されるプロパティ
//		Map<String, AttributeValue> lastEvaluatedKey = null;
//		// 結果取得
//		List<UserEntity> entityList = new ArrayList<>();
//
//		while (true) {
//			// クエリーの実行
//			QueryResponse response = dynamoDbClient.query(queryReq.exclusiveStartKey(lastEvaluatedKey).build());
//
//			// 取得結果の設定
//			for (Map<String, AttributeValue> rec : response.items()) {
//				// キー情報が取得できないことを考慮してデフォルト値を設定する
//				AttributeValue defaultValue = AttributeValue.fromS("");
//				// 取得値
//				UserEntity userEntity = new UserEntity();
//				userEntity.setId(rec.getOrDefault("id", defaultValue).s());
//				entityList.add(userEntity);
//			}
//			// ページング結果が最終である場合はブレイクする
//			lastEvaluatedKey = response.lastEvaluatedKey();
//			if (lastEvaluatedKey.isEmpty()) {
//				break;
//			}
//		}
//
//		return entityList;
//	}
//
//	@Test
//	void sampleUserForDynamodb() {
//		List<UserEntity> userList = userForDynamodb.query("1");
//		assertThat(userList.isEmpty());
//	}
//
//}
