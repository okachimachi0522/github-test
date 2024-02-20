package com.example.post.model.users;

import lombok.Getter;

@Getter
public enum GenderType {
	MALE("남성"),
	FEMALE("여성");
	
	private final String description;
	
	GenderType(String description) {
		this.description = description;
	}
}
