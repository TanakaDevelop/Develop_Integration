package com.example.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.entity.UserEntity;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

@RequiredArgsConstructor
@Repository
public class UserForDynamodb {

	/**
	 * DynamoDbClient
	 */
	private final DynamoDbClient dynamoDbClient;

	/**
	 * Todoテーブル
	 */
	private static final String TABLE_USER = "User";

	/**
	* query 指定のパーテンションキーから値を取得する
	*
	* @param id ID
	* @return List<UserEntity> 
	*/
	public List<UserEntity> query(String id) {

		//　検索キー項目名を設定
		HashMap<String, String> attrNameAlias = new HashMap<String, String>();
		attrNameAlias.put("#id", "id");
		// 検索項目値を設定
		HashMap<String, AttributeValue> attrValues = new HashMap<>();
		attrValues.put(":id", AttributeValue.builder()
				.s(id)
				.build());
		// 検索クエリビルダ
		QueryRequest.Builder queryReq = QueryRequest.builder()
				.tableName(TABLE_USER)
				.keyConditionExpression("#id = :id")
				.expressionAttributeNames(attrNameAlias)
				.expressionAttributeValues(attrValues);

		// 結果のページングを管理するために使用されるプロパティ
		Map<String, AttributeValue> lastEvaluatedKey = null;
		// 結果取得
		List<UserEntity> entityList = new ArrayList<>();

		while (true) {
			// クエリーの実行
			QueryResponse response = dynamoDbClient.query(queryReq.exclusiveStartKey(lastEvaluatedKey).build());

			// 取得結果の設定
			for (Map<String, AttributeValue> rec : response.items()) {
				// キー情報が取得できないことを考慮してデフォルト値を設定する
				AttributeValue defaultValue = AttributeValue.fromS("");
				// 取得値
				UserEntity userEntity = new UserEntity();
				userEntity.setId(rec.getOrDefault("id", defaultValue).s());
				entityList.add(userEntity);
			}
			// ページング結果が最終である場合はブレイクする
			lastEvaluatedKey = response.lastEvaluatedKey();
			if (lastEvaluatedKey.isEmpty()) {
				break;
			}
		}

		return entityList;
	}

}
