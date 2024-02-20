package com.example.post.model.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginForm {
	@NotBlank
	private String username;
	@NotBlank
	private String password;
}
