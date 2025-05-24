package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.UserEntity;
import com.example.repository.UserForDynamodb;
import com.example.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSearchService {

	private final UserForDynamodb userForDynamodb;

	/**
	 * ユーザー検索
	 *
	 * @param id ユーザーID
	 * @return ユーザー情報
	 */
	public UserResponse searchUser(String id) {
		// ユーザー検索
		List<UserEntity> userList = userForDynamodb.query(id);

		return UserResponse.from(userList.get(0));
	}

}
