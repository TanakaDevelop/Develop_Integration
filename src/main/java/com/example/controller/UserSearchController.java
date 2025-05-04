package com.example.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.UserEntity;
import com.example.request.UserInfo;
import com.example.service.UserSearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserSearchController {

	private final UserSearchService userSearchService;

	/**
	 * ユーザー検索
	 *
	 * @param id ユーザーID
	 * @return ユーザー情報
	 */
	@PostMapping("/user/search")
	public List<UserEntity> searchUser(@RequestBody UserInfo userInfo) {
		// ユーザー検索
		return userSearchService.searchUser(userInfo.getId());
	}

}
