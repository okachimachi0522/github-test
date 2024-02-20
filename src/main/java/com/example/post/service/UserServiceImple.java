package com.example.post.service;

import org.springframework.stereotype.Service;

import com.example.post.model.users.User;
import com.example.post.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImple implements UserService {
	
	private final UserRepository userRepository;

	@Override
	public User registerUser(User user) {
		userRepository.saveUser(user);
		return user;
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}
	
	@Override
	public User findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}
	
	@Override
	public User updateUser(User user) {
		userRepository.updateUser(user);
		return user;
	}

}
