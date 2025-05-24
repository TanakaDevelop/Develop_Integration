package com.example.response;

import com.example.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

	public static UserResponse from(UserEntity userEntity) {
		return new UserResponse(userEntity.getId(), userEntity.getName());
	}
}
