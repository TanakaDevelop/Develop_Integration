package com.example.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDBTable(tableName = "User")
@DynamoDbBean
@Getter
@Setter
public class UserEntity {

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名前
	 */
	private String name;

	/**
	 * 歳
	 */
	private String age;

	@DynamoDbPartitionKey
	@DynamoDbAttribute("id")
	public String getId() {
		return id;
	}

	@DynamoDbSortKey
	@DynamoDbAttribute("name")
	public String getName() {
		return name;
	}

	@DynamoDbAttribute("age")
	public String geAge() {
		return age;
	}

}
