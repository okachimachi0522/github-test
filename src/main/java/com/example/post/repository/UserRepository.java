package com.example.post.repository;

import com.example.post.model.users.User;

public interface UserRepository {
	// 회원정보 등록
	void saveUser(User user);
	
	// username으로 회원정보 조회
	User findUserByUsername(String username);
	
	// id로 회원정보 조회
	User findUserById(Long userId);
	
	// 회원정보 수정
	void updateUser(User user);
}
