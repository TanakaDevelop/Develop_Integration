package com.example.entity;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

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
	private String Name;

	@DynamoDbPartitionKey
	@DynamoDbAttribute("id")
	public String getId() {
		return id;
	}

	@DynamoDbSortKey
	@DynamoDbAttribute("Name")
	public String getName() {
		return Name;
	}

}
