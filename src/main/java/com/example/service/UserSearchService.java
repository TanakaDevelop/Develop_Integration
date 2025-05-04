package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.UserEntity;
import com.example.repository.UserForDynamodb;

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
	public List<UserEntity> searchUser(String id) {
		// ユーザー検索
		return userForDynamodb.query(id);
	}

}
